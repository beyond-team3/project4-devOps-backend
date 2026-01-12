package com.aespa.armageddon.core.domain.goal.dto.request;

import com.aespa.armageddon.core.domain.goal.domain.ExpenseCategory;

public record CreateExpenseGoalRequest(
        ExpenseCategory category, // 지출 카테고리
        Integer targetAmount      // 목표 금액 (월 기준)
) {}