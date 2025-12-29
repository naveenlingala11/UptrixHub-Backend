package com.ja.code.debug;

import java.util.Map;

public class AstDebugState {

    public int line;
    public String codeLine;
    public Map<String, Object> variables;
    public boolean finished;

    public AstDebugState(
            int line,
            String codeLine,
            Map<String, Object> variables,
            boolean finished
    ) {
        this.line = line;
        this.codeLine = codeLine;
        this.variables = variables;
        this.finished = finished;
    }
}
