package com.aespa.armageddon.core.domain.transaction.command.application.controller;

import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.auth.security.CustomUserDetails;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionEditRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionWriteRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
@Tag(name = "Transactions", description = "Transaction write endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/write")
    @Operation(summary = "Create transaction")
    public ApiResult<?> writeTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody TransactionWriteRequest request) {

        transactionService.writeTransaction(userDetails.getUserId(), request);
        return ApiResult.success();
    }

    @PutMapping("/edit/{transactionId}")
    @Operation(summary = "Edit transaction")
    public ApiResult<?> editTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long transactionId,
            @RequestBody TransactionEditRequest request
    ) {
        transactionService.editTransaction(userDetails.getUserId(), transactionId, request);
        return ApiResult.success();
    }

    @DeleteMapping("/delete/{transactionId}")
    @Operation(summary = "Delete transaction")
    public ApiResult<?> deleteTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long transactionId
    ) {
        transactionService.deleteTransaction(userDetails.getUserId(), transactionId);
        return ApiResult.success();
    }
}
