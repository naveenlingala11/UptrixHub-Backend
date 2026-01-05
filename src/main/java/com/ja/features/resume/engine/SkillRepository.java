package com.ja.features.resume.engine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillRepository {

    private final Map<String, List<String>> skills;

    public SkillRepository() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        skills = mapper.readValue(
                new ClassPathResource("skills.json").getInputStream(),
                new TypeReference<>() {}
        );
    }

    public Set<String> extract(String text) {
        String lower = text.toLowerCase();
        Set<String> found = new HashSet<>();

        skills.values().forEach(list ->
                list.forEach(skill -> {
                    if (lower.contains(skill.toLowerCase())) {
                        found.add(skill);
                    }
                })
        );
        return found;
    }
}
