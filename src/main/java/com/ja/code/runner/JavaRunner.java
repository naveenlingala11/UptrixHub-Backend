package com.ja.code.runner;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

public class JavaRunner {

    public static ProcessResult run(
            String code,
            String stdin,
            String javaVersion
    ) throws Exception {

        long start = System.currentTimeMillis();

        Path dir = Files.createTempDirectory("java-run");

        String packageName = extractPackage(code);
        String className = extractMainClass(code); // 🔥 FIXED

        Path srcDir = (packageName == null)
                ? dir
                : Files.createDirectories(dir.resolve(packageName.replace(".", "/")));

        Path file = srcDir.resolve(className + ".java");
        Files.writeString(file, code);

        /* ========== COMPILE ========== */
        Process compile = new ProcessBuilder("javac", file.toString())
                .directory(dir.toFile())
                .redirectErrorStream(true)
                .start();

        String compileOut = read(compile);
        if (compile.waitFor() != 0) {
            return ProcessResult.compileError(compileOut, code);
        }

        /* ========== RUN ========== */
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        cmd.add(packageName == null ? className : packageName + "." + className);

        Process run = new ProcessBuilder(cmd)
                .directory(dir.toFile())
                .redirectErrorStream(true)
                .start();

        if (stdin != null && !stdin.isBlank()) {
            run.getOutputStream().write(stdin.getBytes());
            run.getOutputStream().flush();
            run.getOutputStream().close();
        }

        if (!run.waitFor(3, TimeUnit.SECONDS)) {
            run.destroyForcibly();
            return ProcessResult.tle();
        }

        String output = read(run);
        long time = System.currentTimeMillis() - start;

        return ProcessResult.success(output, time, code);
    }


    /* ================= HELPERS ================= */

    private static String extractMainClass(String code) {
        Matcher m = Pattern.compile("class\\s+(\\w+)").matcher(code);
        return m.find() ? m.group(1) : "Main";
    }


    private static String extractPackage(String code) {
        Matcher m = Pattern.compile("package\\s+([\\w\\.]+);").matcher(code);
        return m.find() ? m.group(1) : null;
    }

    private static String read(Process p) throws IOException {
        try (InputStream is = p.getInputStream()) {
            return new String(is.readAllBytes());
        }
    }
}
