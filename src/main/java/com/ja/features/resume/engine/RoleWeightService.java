package com.ja.features.resume.engine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoleWeightService {

    private final Map<String, Map<String, Integer>> roleWeights;

    public RoleWeightService() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        roleWeights = mapper.readValue(
                new ClassPathResource("role-weights.json").getInputStream(),
                new TypeReference<>() {}
        );
    }

    public Map<String, Integer> weightsFor(String role) {
        return roleWeights.getOrDefault(role, Map.of("GENERAL", 100));
    }
}
