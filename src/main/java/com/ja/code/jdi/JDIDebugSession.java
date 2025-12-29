package com.ja.code.jdi;

import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JDIDebugSession {

    private static final Logger log =
            LoggerFactory.getLogger(JDIDebugSession.class);

    private VirtualMachine vm;
    private EventQueue queue;

    private ThreadReference currentThread;
    private StackFrame currentFrame;

    // className -> (line -> BreakpointRequest)
    private final Map<String, Map<Integer, BreakpointRequest>> breakpoints = new HashMap<>();
    private final List<String> watchExpressions = new ArrayList<>();
    private final List<PendingBreakpoint> pendingBreakpoints = new ArrayList<>();

    /* ================= START ================= */

    public void start(String mainClass, String classPath) throws Exception {

        System.out.println("🐞 DEBUG START");

        LaunchingConnector connector =
                Bootstrap.virtualMachineManager().defaultConnector();

        Map<String, Connector.Argument> args = connector.defaultArguments();
        args.get("main").setValue(mainClass);
        args.get("options").setValue("-cp " + classPath);
        args.get("suspend").setValue("true");

        vm = connector.launch(args);
        queue = vm.eventQueue();

        EventSet set = queue.remove();
        for (Event e : set) {
            if (e instanceof VMStartEvent ev) {
                ThreadReference t = ev.thread();

                StepRequest step =
                        vm.eventRequestManager().createStepRequest(
                                t,
                                StepRequest.STEP_LINE,
                                StepRequest.STEP_OVER
                        );
                step.addCountFilter(1);
                step.enable();
            }
        }

        set.resume();
        vm.resume();   // 🔥 REQUIRED
        log.info("▶ VM resumed after start");

        System.out.println("✅ VM started and resumed");
    }

    /* ================= ADD BREAKPOINT ================= */

    public void addBreakpoint(String className, int line) {

        log.info("🔴 addBreakpoint called: {}:{}", className, line);

        if (vm == null) {
            log.error("❌ VM is NULL");
            throw new IllegalStateException("Debug VM not started");
        }

        try {
            breakpoints.computeIfAbsent(className, k -> new HashMap<>());

            List<ReferenceType> types = vm.classesByName(className);

            log.info("📦 Loaded classes for {} = {}", className, types.size());

            if (!types.isEmpty()) {
                installBreakpoint(types.get(0), className, line);
                return;
            }

            log.warn("⏳ Class not loaded yet, registering ClassPrepareRequest");

            EventRequestManager erm = vm.eventRequestManager();
            ClassPrepareRequest cpr = erm.createClassPrepareRequest();
            cpr.addClassFilter(className);
            cpr.enable();

            pendingBreakpoints.add(new PendingBreakpoint(className, line));

        } catch (Exception e) {
            log.error("🔥 Failed to add breakpoint {}:{}",
                    className, line, e);
            throw new RuntimeException(e);
        }
    }

    /* ================= CONDITIONAL BREAKPOINT ================= */

    public void addConditionalBreakpoint(
            String className,
            int line,
            String condition
    ) throws Exception {

        System.out.println("🟡 Conditional breakpoint");
        System.out.println("➡ " + className + ":" + line + " if (" + condition + ")");

        breakpoints.computeIfAbsent(className, k -> new HashMap<>());

        List<ReferenceType> types = vm.classesByName(className);
        if (types.isEmpty()) {
            throw new IllegalStateException("Class not loaded: " + className);
        }

        ReferenceType ref = types.get(0);
        List<Location> locations = ref.locationsOfLine(line);

        if (locations.isEmpty()) {
            throw new IllegalStateException("No executable code at line " + line);
        }

        Location loc = locations.get(0);

        BreakpointRequest bp =
                vm.eventRequestManager().createBreakpointRequest(loc);

        bp.putProperty("condition", condition);
        bp.enable();

        breakpoints
                .computeIfAbsent(className, k -> new HashMap<>())
                .put(line, bp);

        System.out.println("✅ Conditional breakpoint installed");
    }

    private void installBreakpoint(
            ReferenceType ref,
            String className,
            int line
    ) {
        log.info("🔧 Installing breakpoint {}:{}", className, line);

        try {
            List<Location> locations = ref.locationsOfLine(line);

            if (locations == null || locations.isEmpty()) {
                log.warn("⚠ No executable code at {}:{}", className, line);
                return; // 🔥 DO NOT THROW
            }

            // prevent duplicate breakpoint
            Map<Integer, BreakpointRequest> map =
                    breakpoints.computeIfAbsent(className, k -> new HashMap<>());

            if (map.containsKey(line)) {
                log.warn("⚠ Breakpoint already exists at {}:{}", className, line);
                return;
            }

            Location loc = locations.get(0);

            BreakpointRequest bp =
                    vm.eventRequestManager().createBreakpointRequest(loc);

            bp.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
            bp.enable();

            map.put(line, bp);

            log.info("✅ Breakpoint installed at {}:{}", className, line);

        } catch (AbsentInformationException e) {
            log.error("❌ No debug info available for {}", className);
        }
    }
    /* ================= REMOVE BREAKPOINT ================= */

    public void removeBreakpoint(String className, int line) {

        System.out.println("❌ removeBreakpoint " + className + ":" + line);

        Map<Integer, BreakpointRequest> map = breakpoints.get(className);
        if (map == null) return;

        BreakpointRequest bp = map.remove(line);
        if (bp != null) {
            bp.disable();
            vm.eventRequestManager().deleteEventRequest(bp);
            System.out.println("✅ Breakpoint removed");
        }

        if (map.isEmpty()) {
            breakpoints.remove(className);
        }
    }

    /* ================= STEP ================= */

    public DebugState step() throws Exception {

        EventSet events = queue.remove();

        for (Event e : events) {

            if (e instanceof ClassPrepareEvent cp) {
                String name = cp.referenceType().name();

                pendingBreakpoints.removeIf(pb -> {
                    if (pb.className.equals(name)) {
                        try {
                            installBreakpoint(cp.referenceType(), pb.className, pb.line);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return true;
                    }
                    return false;
                });
            }

            if (e instanceof BreakpointEvent be) {
                ThreadReference t = be.thread();
                StackFrame frame = t.frame(0);

                currentThread = t;
                currentFrame = frame;

                DebugState s = new DebugState();
                s.line = frame.location().lineNumber();
                s.source = frame.location().sourceName();
                s.locals = readLocals(frame);
                s.stack = readStack(t);
                s.watches = evalWatches(frame);

                events.resume();
                return s;
            }
        }

        events.resume();
        return null;
    }

    /* ================= HELPERS ================= */

    private Map<String, String> readLocals(StackFrame f) throws Exception {
        Map<String, String> m = new HashMap<>();
        for (LocalVariable v : f.visibleVariables()) {
            Value val = f.getValue(v);
            m.put(v.name(), val == null ? "null" : val.toString());
        }
        return m;
    }

    private List<String> readStack(ThreadReference t) throws Exception {
        List<String> stack = new ArrayList<>();
        for (StackFrame f : t.frames()) {
            stack.add(
                    f.location().declaringType().name() + "." +
                            f.location().method().name() +
                            ":" + f.location().lineNumber()
            );
        }
        return stack;
    }

    private Map<String, String> evalWatches(StackFrame f) {
        Map<String, String> w = new HashMap<>();
        for (String expr : watchExpressions) {
            try {
                LocalVariable v = f.visibleVariableByName(expr);
                if (v != null) {
                    w.put(expr, f.getValue(v).toString());
                }
            } catch (Exception ignored) {}
        }
        return w;
    }

    /* ================= EXPRESSION EVAL ================= */

    public String evalExpression(String expr) {

        if (currentFrame == null) {
            return "❌ No active stack frame";
        }

        try {
            LocalVariable v =
                    currentFrame.visibleVariableByName(expr.trim());

            if (v == null) {
                return "undefined";
            }

            Value val = currentFrame.getValue(v);
            return val == null ? "null" : val.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    static class PendingBreakpoint {
        String className;
        int line;

        PendingBreakpoint(String c, int l) {
            className = c;
            line = l;
        }
    }

    public void stop() {
        if (vm != null) vm.exit(0);
    }
    /* ================= WATCH ================= */

    public void addWatch(String expr) {
        if (expr == null || expr.isBlank()) return;

        System.out.println("👁 Add watch: " + expr);
        watchExpressions.add(expr.trim());
    }
    /* ================= CONTINUE / RESUME ================= */

    public void resume() {
        if (vm == null) {
            System.out.println("⚠ resume() called but VM is null");
            return;
        }

        System.out.println("▶ Resuming VM");
        vm.resume();
    }

}
