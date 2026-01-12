package com.aespa.armageddon.core.domain.cashflow.service;

import com.aespa.armageddon.core.domain.cashflow.dto.IncomeExpenseSum;
import com.aespa.armageddon.core.domain.cashflow.dto.SummaryStatisticsResponse;
import com.aespa.armageddon.core.domain.cashflow.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public SummaryStatisticsResponse getSummary(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate
    ) {
        IncomeExpenseSum sum =
                statisticsRepository.findIncomeExpenseSum(userNo, startDate, endDate);

        long income = sum.totalIncome();
        long expense = sum.totalExpense();

        long netProfit = income - expense;

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long averageExpense = days > 0 ? expense / days : 0;

        return new SummaryStatisticsResponse(
                income,
                expense,
                netProfit,
                averageExpense
        );
    }
}
