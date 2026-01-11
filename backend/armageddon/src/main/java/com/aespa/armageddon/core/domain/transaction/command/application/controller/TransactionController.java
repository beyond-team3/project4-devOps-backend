package com.aespa.armageddon.core.domain.transaction.command.application.controller;

import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.auth.entity.User;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionEditRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionWriteRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/write")
    public ApiResult<?> writeTransaction(
            @RequestBody TransactionWriteRequest request) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        transactionService.writeTransaction(loginId, request);
        return ApiResult.success();
    }

    @PutMapping("/edit/{transactionId}")
    public ApiResult<?> editTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionEditRequest request
    ) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        transactionService.editTransaction(loginId, transactionId, request);
        return ApiResult.success();
    }
}
