package com.aespa.armageddon.core.domain.transaction.query.repository;

import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.transaction.query.dto.QTransactionResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.QTransactionSummaryResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionSummaryResponse;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.QTransaction.transaction;

@Repository
@RequiredArgsConstructor
public class TransactionQueryRepository {

    private final JPAQueryFactory queryFactory; // QueryDSL을 사용하기 위한 JPAQueryFactory 주입

    public List<TransactionResponse> findDailyList(Long userNo, LocalDate date) {
        return queryFactory
                .select(new QTransactionResponse(
                        transaction.transactionId,
                        transaction.title,
                        transaction.amount,
                        transaction.type
                ))
                .from(transaction)
                .where(
                        transaction.userNo.eq(userNo),      // 유저 본인 내역 조회
                        transaction.date.eq(date)           // 요청한 날짜의 내역 조회
                )
                .fetch();
    }

    /* 월간 총 수입/지출/잔액 요약 조회 */
    public TransactionSummaryResponse findMonthlySummary(Long userNo, int year, int month) {

        // 검색할 월의 시작일과 마지막일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return queryFactory
                .select(new QTransactionSummaryResponse(
                        // 1. 수입 합계
                        new CaseBuilder()
                                .when(transaction.type.eq(TransactionType.INCOME))
                                .then(transaction.amount.longValue()) // int -> long 타입변환
                                .otherwise(0L)               // 조건 맞지 않으면 0
                                .sum(),                              // 합계

                        // 2. 지출 합계
                        new CaseBuilder()
                                .when(transaction.type.eq(TransactionType.EXPENSE))
                                .then(transaction.amount.longValue())
                                .otherwise(0L)
                                .sum()
                ))
                .from(transaction)
                .where(
                        transaction.userNo.eq(userNo),
                        transaction.date.between(startDate, endDate)    // 해당 월 데이터만
                )
                .fetchOne();
    }
}
