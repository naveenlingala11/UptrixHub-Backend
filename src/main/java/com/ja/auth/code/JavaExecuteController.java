package com.ja.auth.code;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/execute")
@CrossOrigin
public class JavaExecuteController {

    @PostMapping("/java")
    public Map<String, Object> execute(@RequestBody Map<String, String> body) {

        Map<String, Object> result = new HashMap<>();

        try {
            JavaRunner.Result r = JavaRunner.run(body.get("code"));
            result.put("output", r.output);
            result.put("wrappedCode", r.wrappedCode);
        } catch (Exception e) {
            result.put("output", "❌ Error: " + e.getMessage());
        }

        return result;
    }

}
