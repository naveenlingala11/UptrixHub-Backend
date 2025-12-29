package com.ja.code.controller;

import com.ja.code.jdi.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin
public class DebugController {

    private JDIDebugSession session;

    @PostMapping("/start")
    public void start(@RequestParam String mainClass,
                      @RequestParam String classPath) throws Exception {
        session = new JDIDebugSession();
        session.start(mainClass, classPath);
    }

    @PostMapping("/step")
    public DebugState step() throws Exception {

        if (session == null) {
            throw new IllegalStateException("Debug session not started");
        }

        DebugState s = session.step();

        if (s != null && s.finished) {
            session = null;
        }

        return s;
    }

    @PostMapping("/stop")
    public void stop() {
        if (session != null) {
            session.stop();
            session = null;
        }
    }

    @PostMapping("/watch")
    public void watch(@RequestParam String expr) {
        session.addWatch(expr);
    }

    @PostMapping("/breakpoint/conditional")
    public void conditionalBreakpoint(
            @RequestParam String className,
            @RequestParam int line,
            @RequestParam String condition
    ) throws Exception {
        session.addConditionalBreakpoint(className, line, condition);
    }

    @PostMapping("/eval")
    public String eval(@RequestParam String expr) {

        if (session == null) {
            return "❌ Debug session not active";
        }

        return session.evalExpression(expr);
    }

    @PostMapping("/breakpoint/remove")
    public void remove(
            @RequestParam String className,
            @RequestParam int line
    ) {
        session.removeBreakpoint(className, line);
    }

    @PostMapping("/breakpoint/add")
    public ResponseEntity<?> addBreakpoint(
            @RequestParam String className,
            @RequestParam int line
    ) {
        if (session == null) {
            return ResponseEntity
                    .status(409)
                    .body("Debug session not started");
        }

        try {
            session.addBreakpoint(className, line);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body("Invalid breakpoint location");
        }
    }

}
