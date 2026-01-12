package com.aespa.armageddon.core.domain.transaction.command.application.controller;

import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.auth.entity.User;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionEditRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionWriteRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.service.TransactionService;
import com.aespa.armageddon.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/write")
    public ApiResult<?> writeTransaction(
            @RequestHeader("Authorization") String authorization,
            @RequestBody TransactionWriteRequest request) {

        String token = authorization.substring(7);
        Long userNo = jwtTokenProvider.getUserIdFromJWT(token);
        transactionService.writeTransaction(userNo, request);
        return ApiResult.success();
    }

    @PutMapping("/edit/{transactionId}")
    public ApiResult<?> editTransaction(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long transactionId,
            @RequestBody TransactionEditRequest request
    ) {
        String token = authorization.substring(7);
        Long userNo = jwtTokenProvider.getUserIdFromJWT(token);
        transactionService.editTransaction(userNo, transactionId, request);
        return ApiResult.success();
    }

    @DeleteMapping("/delete/{transactionId}")
    public ApiResult<?> deleteTransaction(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long transactionId
    ) {
        String token = authorization.substring(7);
        Long userNo = jwtTokenProvider.getUserIdFromJWT(token);

        transactionService.deleteTransaction(userNo, transactionId);
        return ApiResult.success();
    }
}
