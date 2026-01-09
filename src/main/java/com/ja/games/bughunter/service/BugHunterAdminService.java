package com.ja.games.games.bughunter.service;

import com.ja.games.games.bughunter.entity.BugHunterQuestion;
import com.ja.games.games.bughunter.repository.BugHunterQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BugHunterAdminService {

    private final BugHunterQuestionRepository repo;

    /** 🔁 CREATE NEW VERSION */
    public BugHunterQuestion createNewVersion(
            Long originalId,
            BugHunterQuestion updated
    ) {
        BugHunterQuestion original =
                repo.findById(originalId).orElseThrow();

        BugHunterQuestion v = new BugHunterQuestion();

        v.setParentId(original.getParentId() != null
                ? original.getParentId()
                : original.getId());

        v.setVersion(original.getVersion() + 1);

        v.setTitle(updated.getTitle());
        v.setLanguage(updated.getLanguage());
        v.setDifficulty(updated.getDifficulty());
        v.setCode(updated.getCode());
        v.setBugType(updated.getBugType());
        v.setReason(updated.getReason());
        v.setFix(updated.getFix());
        v.setXp(updated.getXp());

        v.setPublished(false); // 🔒 admin decides
        v.setActive(true);

        return repo.save(v);
    }

    @Transactional
    public void publish(Long id) {

        BugHunterQuestion q = repo.findById(id).orElseThrow();

        Long rootId = q.getParentId() != null ? q.getParentId() : q.getId();

        // unpublish old versions
        repo.findByParentIdAndPublishedTrue(rootId)
                .forEach(old -> {
                    old.setPublished(false);
                    repo.save(old);
                });

        q.setPublished(true);
        repo.save(q);
    }

}
