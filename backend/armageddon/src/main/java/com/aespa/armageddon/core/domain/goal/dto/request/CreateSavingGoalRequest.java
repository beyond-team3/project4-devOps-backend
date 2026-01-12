package com.aespa.armageddon.core.domain.goal.dto.request;

import java.time.LocalDate;

public record CreateSavingGoalRequest(
        Integer targetAmount,   // 목표 금액
        LocalDate startDate,    // 시작일
        LocalDate endDate       // 종료일
) {}
