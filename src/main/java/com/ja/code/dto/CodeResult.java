package com.ja.code.dto;

import lombok.Builder;

@Builder
public class CodeResult {
    public boolean compiled;
    public boolean executed;
    public String output;
    public String verdict;
    public long timeMs;
    public long memoryKb;
}
