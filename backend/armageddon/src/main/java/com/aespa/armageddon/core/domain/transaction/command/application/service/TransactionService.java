package com.aespa.armageddon.core.domain.transaction.command.application.service;

import com.aespa.armageddon.core.common.support.error.CoreException;
import com.aespa.armageddon.core.common.support.error.ErrorType;
import com.aespa.armageddon.core.domain.auth.repository.UserRepository;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionEditRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionWriteRequest;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Transaction;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.transaction.command.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writeTransaction(String loginId, TransactionWriteRequest request) {

        Long userNo = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CoreException(ErrorType.USER_NOT_FOUND))
                .getId();

        Transaction transaction = new Transaction(
                userNo,
                request.title(),
                request.memo(),
                request.amount(),
                request.date(),
                request.type(),
                request.category()
        );

        transactionRepository.save(transaction);
    }

    @Transactional
    public void editTransaction(String loginId, Long transactionId, TransactionEditRequest request) {

        Long userNo = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CoreException(ErrorType.USER_NOT_FOUND))
                .getId();

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CoreException(ErrorType.TRANSACTION_NOT_FOUND));

        if (!transaction.getUserNo().equals(userNo)) {
            throw new CoreException(ErrorType.ACCESS_DENIED);
        }

        transaction.edit(
                request.title(),
                request.memo(),
                request.amount(),
                request.date(),
                request.type(),
                request.category()
        );
    }

}
