package com.aespa.armageddon.core.domain.transaction.query.repository;

import com.aespa.armageddon.core.domain.transaction.query.dto.QTransactionResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionResponse;
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
}
