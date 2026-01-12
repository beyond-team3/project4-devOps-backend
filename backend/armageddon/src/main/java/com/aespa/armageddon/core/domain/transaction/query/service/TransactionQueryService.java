package com.aespa.armageddon.core.domain.transaction.query.service;

import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionSummaryResponse;
import com.aespa.armageddon.core.domain.transaction.query.repository.TransactionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)     // 조회 전용 (성능 최적화 & 데이터 변경 실수 방지)
public class TransactionQueryService {

    private final TransactionQueryRepository transactionQueryRepository;


    /*
    * 일간 가계부 내역 조회
    * 요청을 받으면 Repository로 전달
    * */
    public List<TransactionResponse> getDailyTransactions(Long userNo, LocalDate date) {

        return transactionQueryRepository.findDailyList(userNo, date);

    }

    /**
     * 월간 요약 정보 조회 (수입, 지출, 잔액)
     */
    public TransactionSummaryResponse getMonthlySummary(Long userNo, int year, int month) {

        return transactionQueryRepository.findMonthlySummary(userNo, year, month);

    }
}
