package com.aespa.armageddon.core.domain.cashflow.repository;

import com.aespa.armageddon.core.domain.cashflow.dto.IncomeExpenseSum;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StatisticsRepository {

    IncomeExpenseSum findIncomeExpenseSum(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate
    );
}