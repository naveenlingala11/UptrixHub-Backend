package com.ja.features.resume.engine;

import java.util.*;

public class SkillDictionary {

    public static final Map<String, List<String>> SKILLS = Map.ofEntries(

            Map.entry("LANGUAGES", List.of(
                    "Java","Python","JavaScript","TypeScript","C++","C#","Go","Rust","Kotlin","Swift","PHP","Ruby"
            )),

            Map.entry("FRONTEND", List.of(
                    "HTML","CSS","React","Angular","Vue","Next.js","Tailwind","Bootstrap"
            )),

            Map.entry("BACKEND", List.of(
                    "Spring","Spring Boot","Node.js","Express","Django","Flask","FastAPI",".NET","Laravel"
            )),

            Map.entry("DATABASE", List.of(
                    "MySQL","PostgreSQL","MongoDB","Redis","Oracle","Cassandra","DynamoDB"
            )),

            Map.entry("CLOUD", List.of(
                    "AWS","Azure","GCP","EC2","S3","Lambda","Cloud Functions"
            )),

            Map.entry("DEVOPS", List.of(
                    "Docker","Kubernetes","CI/CD","Jenkins","GitHub Actions","Terraform","Ansible"
            )),

            Map.entry("DATA", List.of(
                    "SQL","Spark","Hadoop","Kafka","Airflow","ETL"
            )),

            Map.entry("AI_ML", List.of(
                    "Machine Learning","Deep Learning","NLP","TensorFlow","PyTorch","OpenAI","LLM"
            )),

            Map.entry("TESTING", List.of(
                    "JUnit","Mockito","Selenium","Cypress","Playwright","Postman"
            )),

            Map.entry("TOOLS", List.of(
                    "Git","GitHub","Jira","Confluence","Linux","IntelliJ","VS Code"
            )),

            Map.entry("ARCHITECTURE", List.of(
                    "Microservices","System Design","REST","GraphQL","Event Driven","Monolith"
            ))
    );
}
