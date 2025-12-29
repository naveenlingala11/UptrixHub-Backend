package com.ja.code.runner;

public record ProcessResult(
        boolean success,
        String output,
        String error,
        long timeMs,
        String wrappedCode
) {
    public static ProcessResult success(String output, long time, String wrapped) {
        return new ProcessResult(true, output, null, time, wrapped);
    }

    public static ProcessResult compileError(String err, String wrapped) {
        return new ProcessResult(false, null, err, 0, wrapped);
    }

    public static ProcessResult tle() {
        return new ProcessResult(false, null, "❌ Time Limit Exceeded", 0, null);
    }
}
