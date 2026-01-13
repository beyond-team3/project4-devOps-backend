package com.aespa.armageddon.core.domain.goal.dto.request;

import java.time.LocalDate;

public record UpdateGoalRequest(
        String title,
        Integer targetAmount,   // 수정할 목표 금액
        LocalDate endDate       // 저축 목표만 사용 (지출 목표는 null)
) {}
