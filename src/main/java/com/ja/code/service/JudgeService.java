package com.ja.code.service;

import com.ja.code.dto.*;
import com.ja.code.runner.JavaRunner;
import com.ja.code.runner.ProcessResult;

public class JudgeService {

    public CodeResult judge(CodeRequest req) throws Exception {

        int pass = 0;

        for (TestCase t : req.getTests()) {

            ProcessResult res = JavaRunner.run(
                    req.getCode(),
                    t.getInput(),
                    req.getJavaVersion()
            );

            if (!res.output().trim().equals(t.getExpectedOutput().trim())) {
                return CodeResult.builder()
                        .verdict("❌ Wrong Answer")
                        .executed(true)
                        .build();
            }
            pass++;
        }

        return CodeResult.builder()
                .verdict("✅ Accepted (" + pass + "/" + req.getTests().size() + ")")
                .executed(true)
                .build();
    }
}
