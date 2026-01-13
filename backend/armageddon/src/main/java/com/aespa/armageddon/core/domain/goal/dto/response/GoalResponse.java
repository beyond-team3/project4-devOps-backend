package com.aespa.armageddon.core.domain.goal.dto.response;

import com.aespa.armageddon.core.domain.goal.domain.GoalStatus;
import com.aespa.armageddon.core.domain.goal.domain.GoalType;

public record GoalResponse(
        Long goalId,
        GoalType goalType,       // SAVING / EXPENSE
        String title,            // 목표 이름 (선택)
        Integer targetAmount,
        Integer progressRate,    // 달성률 (%)
        GoalStatus status        // ACTIVE / COMPLETED
) {}