package com.ja.games.bughunter.repository;

import com.ja.games.bughunter.dto.BugQuestionAnalytics;
import com.ja.games.bughunter.entity.BugHunterQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BugHunterQuestionRepository
        extends JpaRepository<BugHunterQuestion, Long> {

    List<BugHunterQuestion> findByLanguageAndActiveTrue(String language);

    Optional<BugHunterQuestion>
    findTopByParentIdOrderByVersionDesc(Long parentId);

    List<BugHunterQuestion> findByParentIdOrderByVersionDesc(Long parentId);

    List<BugHunterQuestion> findByParentIdAndPublishedTrue(Long parentId);

    List<BugHunterQuestion> findByPublishedTrueAndActiveTrue();

    @Query("""
select new com.ja.games.bughunter.dto.BugQuestionAnalytics(
 q.id,
 q.title,
 count(a),
 sum(case when a.correct = true then 1 else 0 end),
 (sum(case when a.correct = true then 1 else 0 end) * 100.0 / count(a))
)
from BugHunterAttempt a
join BugHunterQuestion q on a.questionId = q.id
group by q.id, q.title
""")
    List<BugQuestionAnalytics> analytics();


    @Query("""
select q from BugHunterQuestion q
where q.language = :lang
and q.difficulty = :diff
and q.active = true
and q.published = true
order by random()
limit :limit
""")
    List<BugHunterQuestion> random(
            String lang,
            String diff,
            int limit
    );


}
