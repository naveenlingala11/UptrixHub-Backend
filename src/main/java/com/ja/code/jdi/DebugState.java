package com.ja.code.jdi;

import java.util.List;
import java.util.Map;

public class DebugState {

    public int line;
    public String source;
    public Map<String, String> locals;
    public List<String> stack;
    public Map<String, String> watches;
    public boolean finished;

    public static DebugState finished() {
        DebugState s = new DebugState();
        s.finished = true;
        return s;
    }
}
