package com.aespa.armageddon.core.domain.cashflow.repository;

import com.aespa.armageddon.core.domain.cashflow.dto.CategoryExpenseSum;
import com.aespa.armageddon.core.domain.cashflow.dto.IncomeExpenseSum;
import com.aespa.armageddon.core.domain.cashflow.dto.IncomeExpenseSum;
import com.aespa.armageddon.core.domain.cashflow.dto.TopExpenseItemResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatisticsRepository {

    IncomeExpenseSum findIncomeExpenseSum(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate
    );

    List<CategoryExpenseSum> findCategoryExpenseSum(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate
    );

    List<TopExpenseItemResponse> findTopExpenseItems(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate,
            int limit
    );
}