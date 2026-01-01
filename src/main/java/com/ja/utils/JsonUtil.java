package com.ja.utils;

import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class JsonUtil {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<String> readList(String json) {
        try {
            if (json == null || json.isBlank()) return List.of();
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    public String writeList(List<String> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }
}
