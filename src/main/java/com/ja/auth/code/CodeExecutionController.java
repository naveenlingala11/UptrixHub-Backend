package com.ja.auth.code;

import com.ja.code.dto.CodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/compile")
@CrossOrigin
public class CodeExecutionController {

    @PostMapping
    public ResponseEntity<?> compile(@RequestBody CodeRequest req) {
        try {
            String fileName = "Main.java";
            Path tempDir = Files.createTempDirectory("java-run");

            Path source = tempDir.resolve(fileName);
            Files.writeString(source, req.getCode());

            Process compile = new ProcessBuilder(
                    "javac", fileName
            )
                    .directory(tempDir.toFile())
                    .start();

            compile.waitFor();

            String compileError = new String(compile.getErrorStream().readAllBytes());
            if (!compileError.isEmpty()) {
                return ResponseEntity.ok(
                        Map.of("output", compileError)
                );
            }

            Process run = new ProcessBuilder(
                    "java", "Main"
            )
                    .directory(tempDir.toFile())
                    .start();

            String output = new String(run.getInputStream().readAllBytes());
            return ResponseEntity.ok(Map.of("output", output));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("output", e.getMessage()));
        }
    }
}
