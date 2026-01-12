package com.aespa.armageddon.core.domain.cashflow.dto;

public record SummaryStatisticsResponse(
        long totalIncome,     // 총 수입
        long totalExpense,    // 총 지출
        long netProfit,       // 순수익 (수입 - 지출)
        long averageExpense   // 평균 지출
) {
}
