package com.ja.games.bughunter.dto;

import lombok.Data;
import java.util.List;

@Data
public class SubmitBugHunterBulkRequest {
    private List<BugHunterQuestionRequest> questions;
}
