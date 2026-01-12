package com.aespa.armageddon.core.domain.cashflow.repository;

import com.aespa.armageddon.core.domain.cashflow.dto.IncomeExpenseSum;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryImpl implements StatisticsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public IncomeExpenseSum findIncomeExpenseSum(
            Long userNo,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Object[] result = (Object[]) em.createQuery("""
            SELECT
              COALESCE(SUM(CASE WHEN t.type = :income THEN t.amount ELSE 0 END), 0),
              COALESCE(SUM(CASE WHEN t.type = :expense THEN t.amount ELSE 0 END), 0)
            FROM Transaction t
            WHERE t.userNo = :userNo
            AND t.date BETWEEN :start AND :end
        """)
                .setParameter("income", TransactionType.INCOME)
                .setParameter("expense", TransactionType.EXPENSE)
                .setParameter("userNo", userNo)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getSingleResult();

        return new IncomeExpenseSum(
                ((Number) result[0]).longValue(),
                ((Number) result[1]).longValue()
        );
    }
}
