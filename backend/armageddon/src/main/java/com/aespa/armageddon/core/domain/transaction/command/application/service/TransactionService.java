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

}
