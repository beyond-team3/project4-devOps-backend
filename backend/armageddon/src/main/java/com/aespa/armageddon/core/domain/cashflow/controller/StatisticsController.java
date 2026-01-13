package com.aespa.armageddon.core.domain.cashflow.controller;

import com.aespa.armageddon.core.common.support.error.CoreException;
import com.aespa.armageddon.core.common.support.error.ErrorType;
import com.aespa.armageddon.core.domain.cashflow.dto.*;
import com.aespa.armageddon.core.domain.cashflow.service.StatisticsService;
import com.aespa.armageddon.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/summary")
    public ResponseEntity<SummaryStatisticsResponse> getSummaryStatistics(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        Long userNo = extractUserNo(authorization);

        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getSummary(userNo, startDate, endDate)
        );
    }

    @GetMapping("/expense/categories")
    public ResponseEntity<List<CategoryExpenseRatio>> getCategoryExpenseStatistics(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        Long userNo = extractUserNo(authorization);

        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getCategoryExpenseWithRatio(
                        userNo, startDate, endDate
                )
        );
    }

    /**
     * 상위 지출 항목 조회
     */
    @GetMapping("/expense/top")
    public ResponseEntity<List<TopExpenseItemResponse>> getTopExpenseItems(
            @RequestHeader("Authorization") String authorization,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(required = false)
            Integer limit
    ) {
        Long userNo = extractUserNo(authorization);

        return ResponseEntity.ok(
                statisticsService.getTopExpenseItems(
                        userNo,
                        startDate,
                        endDate,
                        limit
                )
        );
    }

    @GetMapping("/expense/trend")
    public ResponseEntity<ExpenseTrendResponse> getExpenseTrend(
            @RequestHeader("Authorization") String authorization,

            @RequestParam TrendUnit unit,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        Long userNo = extractUserNo(authorization);

        // 기본값: 이번 달
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getExpenseTrend(
                        userNo,
                        startDate,
                        endDate,
                        unit
                )
        );
    }

    //공통 메서드
    private Long extractUserNo(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }

        String token = authorization.substring(7);
        return jwtTokenProvider.getUserIdFromJWT(token);
    }


}
