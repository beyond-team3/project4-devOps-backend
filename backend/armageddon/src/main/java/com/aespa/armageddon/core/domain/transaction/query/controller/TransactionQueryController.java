package com.aespa.armageddon.core.domain.transaction.query.controller;

import com.aespa.armageddon.core.domain.auth.entity.User;
import com.aespa.armageddon.core.domain.auth.repository.UserRepository;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionSummaryResponse;
import com.aespa.armageddon.core.domain.transaction.query.service.TransactionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
@Tag(name = "Transactions", description = "Transaction query endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TransactionQueryController {

    private final TransactionQueryService transactionQueryService;
    private final UserRepository userRepository;

    /* 일간 상세 내역 조회 (날짜 클릭 시 리스트) */
    @GetMapping("/daily")
    @Operation(summary = "Get daily transactions")
    public ResponseEntity<List<TransactionResponse>> getDailyTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Date to query (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(transactionQueryService.getDailyTransactions(user.getId(), date));
    }

    /* 월간 요약 정보 조회 (수입, 지출, 잔액) */
    @GetMapping("/monthly")
    @Operation(summary = "Get monthly transaction summary")
    public ResponseEntity<TransactionSummaryResponse> getMonthlySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Year (e.g. 2025)")
            @RequestParam int year,
            @Parameter(description = "Month (1-12)")
            @RequestParam int month) {

        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(transactionQueryService.getMonthlySummary(user.getId(), year, month));
    }
}
