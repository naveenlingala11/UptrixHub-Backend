package com.ja.features.resume.engine;

import java.util.*;

public class SkillExtractor {

    public static Set<String> extract(String text) {
        String lower = text.toLowerCase();

        Set<String> found = new HashSet<>();

        SkillDictionary.SKILLS.values()
                .forEach(list ->
                        list.forEach(skill -> {
                            if (lower.contains(skill.toLowerCase())) {
                                found.add(skill);
                            }
                        })
                );

        return found;
    }
}
