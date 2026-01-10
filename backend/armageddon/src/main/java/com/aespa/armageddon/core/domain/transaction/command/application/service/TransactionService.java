package com.aespa.armageddon.core.domain.transaction.command.application.service;

import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionWriteRequest;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Transaction;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.transaction.command.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void writeTransaction(TransactionWriteRequest request) {

        validateRequest(request);

        Transaction transaction = new Transaction(
                request.userNo(),
                request.title(),
                request.memo(),
                request.amount(),
                request.date(),
                request.type(),
                request.category()
        );

        transactionRepository.save(transaction);
    }

    private void validateRequest(TransactionWriteRequest request) {

        if (request.userNo() == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }

        if (request.amount() <= 0) {
            throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        }

        if (request.date() == null) {
            throw new IllegalArgumentException("날짜는 필수입니다.");
        }

        if (request.type() == null) {
            throw new IllegalArgumentException("지출/수입 타입은 필수입니다.");
        }

        if (request.type() == TransactionType.EXPENSE && request.category() == null) {
            throw new IllegalArgumentException("지출일 경우 카테고리는 필수입니다.");
        }

        if (request.type() == TransactionType.INCOME && request.category() != null) {
            throw new IllegalArgumentException("수입에는 카테고리를 입력할 수 없습니다.");
        }
    }
}
