package com.aespa.armageddon.core.domain.cashflow.service;

import com.aespa.armageddon.core.domain.cashflow.dto.*;
import com.aespa.armageddon.core.domain.cashflow.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    public List<CategoryExpenseRatio> getCategoryExpenseWithRatio(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<CategoryExpenseSum> sums =
                statisticsRepository.findCategoryExpenseSum(
                        userNo, startDate, endDate
                );

        long totalExpense = sums.stream()
                .mapToLong(CategoryExpenseSum::totalExpense)
                .sum();

        if (totalExpense == 0) {
            return List.of();
        }

        return sums.stream()
                .map(sum -> new CategoryExpenseRatio(
                        sum.category(),
                        sum.totalExpense(),
                        (double) sum.totalExpense() * 100 / totalExpense
                ))
                .toList();
    }

    public List<TopExpenseItemResponse> getTopExpenseItems(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate,
            Integer limit
    ) {
        // 기본 기간: 이번 달
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        // 기본 limit
        int resultLimit = (limit == null || limit <= 0) ? 5 : limit;

        return statisticsRepository.findTopExpenseItems(
                userNo,
                startDate,
                endDate,
                resultLimit
        );
    }
}
