package com.ja.pseudo.service;

import com.ja.pseudo.dto.QuestionUploadResponse;
import com.ja.pseudo.entity.PseudoQuestion;
import com.ja.pseudo.entity.PseudoSkill;
import com.ja.pseudo.repository.PseudoQuestionRepository;
import com.ja.pseudo.repository.PseudoSkillRepository;
import com.ja.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PseudoQuestionUploadService {

    private final PseudoSkillRepository skillRepo;
    private final PseudoQuestionRepository questionRepo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonUtil jsonUtil = new JsonUtil();

    public QuestionUploadResponse upload(
            MultipartFile file,
            String skillSlug
    ) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "File is empty");
        }

        PseudoSkill skill = skillRepo
                .findBySlugAndActiveTrue(skillSlug)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Invalid skill slug"));

        List<Map<String, Object>> rows = parse(file);

        int success = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            try {
                Map<String, Object> r = rows.get(i);

                validate(r, i);

                PseudoQuestion q = new PseudoQuestion();
                q.setSkill(skill);
                q.setQuestion((String) r.get("question"));

                q.setOptionA((String) r.get("A"));
                q.setOptionB((String) r.get("B"));
                q.setOptionC((String) r.get("C"));
                q.setOptionD((String) r.get("D"));

                q.setCorrectOption(
                        ((String) r.get("correct")).charAt(0));

                q.setDifficulty((String) r.get("difficulty"));
                q.setAccessLevel((String) r.get("access"));

                // 🧠 Explanation
                q.setExplanationSummary((String) r.get("exp_summary"));
                q.setExplanationStepsJson(
                        jsonUtil.writeList((List<String>) r.get("exp_steps"))
                );
                q.setExplanationConceptsJson(
                        jsonUtil.writeList((List<String>) r.get("exp_concepts"))
                );


                // 🔁 backward compatibility
                q.setExplanation(q.getExplanationSummary());

                questionRepo.save(q);
                success++;

            } catch (Exception e) {
                log.error("Upload error at row {}", i + 1, e);
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        return new QuestionUploadResponse(
                rows.size(),
                success,
                rows.size() - success,
                errors
        );
    }

    /* =======================
       PARSERS (UNCHANGED)
       ======================= */

    private List<Map<String, Object>> parse(MultipartFile file)
            throws Exception {

        if (file.getOriginalFilename().endsWith(".json")) {
            return parseJson(file);
        }
        return parseCsv(file);
    }

    private List<Map<String, Object>> parseJson(MultipartFile file)
            throws Exception {

        JsonNode root = mapper.readTree(file.getInputStream());
        List<Map<String, Object>> list = new ArrayList<>();

        for (JsonNode q : root.get("questions")) {

            Map<String, Object> map = new HashMap<>();

            map.put("question", q.get("question").asText());

            map.put("A", q.get("options").get(0).asText());
            map.put("B", q.get("options").get(1).asText());
            map.put("C", q.get("options").get(2).asText());
            map.put("D", q.get("options").get(3).asText());

            map.put("correct", q.get("correct").asText());
            map.put("difficulty", q.get("difficulty").asText());
            map.put("access", q.get("access").asText());

            JsonNode exp = q.get("explanation");

            map.put("exp_summary", exp.get("summary").asText());

            List<String> steps = new ArrayList<>();
            exp.get("steps").forEach(s -> steps.add(s.asText()));
            map.put("exp_steps", steps);

            List<String> concepts = new ArrayList<>();
            exp.get("concepts").forEach(c -> concepts.add(c.asText()));
            map.put("exp_concepts", concepts);

            list.add(map);
        }

        return list;
    }

    private List<Map<String, Object>> parseCsv(MultipartFile file)
            throws Exception {

        List<Map<String, Object>> list = new ArrayList<>();
        BufferedReader br =
                new BufferedReader(new InputStreamReader(file.getInputStream()));

        String headerLine = br.readLine();
        if (headerLine == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "CSV header missing");
        }

        String[] headers = headerLine.split(",");

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");

            Map<String, Object> row = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                row.put(headers[i].trim(), values[i].trim());
            }
            list.add(row);
        }

        return list;
    }

    /* =======================
       VALIDATION
       ======================= */

    private void validate(Map<String, Object> r, int index) {

        if (!List.of("A","B","C","D").contains(r.get("correct")))
            throw new RuntimeException("Invalid correct option");

        if (!List.of("FREE","PRO").contains(r.get("access")))
            throw new RuntimeException("Invalid access");

        if (((String) r.get("question")).length() < 10)
            throw new RuntimeException("Question too short");
    }
}
