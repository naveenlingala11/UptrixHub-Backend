package com.ja.auth.code;

import java.nio.file.Files;
import java.nio.file.Path;

public class JavaRunner {

    public static class Result {
        public String output;
        public String wrappedCode;

        public Result(String output, String wrappedCode) {
            this.output = output;
            this.wrappedCode = wrappedCode;
        }
    }

    public static Result run(String userCode) throws Exception {

        boolean alreadyWrapped =
                userCode.contains("class Main") &&
                        userCode.contains("main(String");

        String finalCode;

        if (alreadyWrapped) {
            // ✅ Use user code as-is
            finalCode = userCode;
        } else {
            // ✅ Safe wrapping
            finalCode = """
            public class Main {
                public static void main(String[] args) throws Exception {
            %s
                }
            }
            """.formatted(indent(userCode));
        }

        Path tempDir = Files.createTempDirectory("java-run");
        Path source = tempDir.resolve("Main.java");
        Files.writeString(source, finalCode);

        // Compile
        Process compile = new ProcessBuilder("javac", "Main.java")
                .directory(tempDir.toFile())
                .redirectErrorStream(true)
                .start();

        String compileOutput = new String(compile.getInputStream().readAllBytes());
        compile.waitFor();

        if (!compileOutput.isBlank()) {
            return new Result("❌ Compile Error:\n" + compileOutput, finalCode);
        }

        // Run
        Process run = new ProcessBuilder("java", "Main")
                .directory(tempDir.toFile())
                .redirectErrorStream(true)
                .start();

        String output = new String(run.getInputStream().readAllBytes());

        return new Result(
                output.isBlank() ? "⚠ No output" : output,
                finalCode
        );
    }

    private static String indent(String code) {
        return code
                .lines()
                .map(line -> "        " + line)
                .reduce("", (a, b) -> a + b + "\n");
    }
}
