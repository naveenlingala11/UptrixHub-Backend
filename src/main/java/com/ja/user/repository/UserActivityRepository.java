package com.ja.user.repository;

import com.ja.user.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserActivityRepository
        extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
select a.date as d, sum(a.count) as c
from UserDailyActivity a
group by a.date
order by a.date
""")
    List<Object[]> aggregateDailyActivity();

}
