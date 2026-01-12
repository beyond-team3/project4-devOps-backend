package com.aespa.armageddon.core.domain.goal.repository;

import com.aespa.armageddon.core.domain.goal.domain.Goal;
import com.aespa.armageddon.core.domain.goal.domain.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal,Integer> {
    List<Goal> findByUserIdAndStatusNot(Long userId, GoalStatus status);

    Optional<Goal> findByGoalIdAndUserId(Long goalId, Long userId);
}
