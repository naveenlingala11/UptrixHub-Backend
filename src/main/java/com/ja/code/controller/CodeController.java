package com.ja.code.controller;

import com.ja.code.dto.*;
import com.ja.code.runner.JavaRunner;
import com.ja.code.runner.ProcessResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
@CrossOrigin
public class CodeController {

    @PostMapping("/java")
    public CodeResponse runJava(@RequestBody CodeRequest req) {

        if (req == null || req.getCode() == null || req.getCode().isBlank()) {
            return CodeResponse.error("Code cannot be empty");
        }

        try {
            ProcessResult result = JavaRunner.run(
                    req.getCode(),
                    req.getInput(),
                    req.getJavaVersion()
            );

            if (!result.success()) {
                return CodeResponse.error(result.error());
            }

            return CodeResponse.success(
                    result.output(),
                    result.wrappedCode(),
                    result.timeMs()
            );

        } catch (Exception e) {
            return CodeResponse.error("❌ Runtime Error:\n" + e.getMessage());
        }
    }
}
