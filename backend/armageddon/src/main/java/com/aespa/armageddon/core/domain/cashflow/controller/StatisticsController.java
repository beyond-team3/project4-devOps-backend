package com.aespa.armageddon.core.domain.cashflow.controller;

import com.aespa.armageddon.core.domain.auth.security.CustomUserDetails;
import com.aespa.armageddon.core.domain.cashflow.dto.*;
import com.aespa.armageddon.core.domain.cashflow.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Cashflow statistics endpoints")
@SecurityRequirement(name = "bearerAuth")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/summary")
    @Operation(summary = "Get summary statistics")
    public ResponseEntity<SummaryStatisticsResponse> getSummaryStatistics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getSummary(userDetails.getUserId(), startDate, endDate)
        );
    }

    @GetMapping("/expense/categories")
    @Operation(summary = "Get expense ratio by category")
    public ResponseEntity<List<CategoryExpenseRatio>> getCategoryExpenseStatistics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getCategoryExpenseWithRatio(
                        userDetails.getUserId(), startDate, endDate
                )
        );
    }

    /**
     * 상위 지출 항목 조회
     */
    @GetMapping("/expense/top")
    @Operation(summary = "Get top expense items")
    public ResponseEntity<List<TopExpenseItemResponse>> getTopExpenseItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @Parameter(description = "Max number of items to return")
            @RequestParam(required = false)
            Integer limit
    ) {
        return ResponseEntity.ok(
                statisticsService.getTopExpenseItems(
                        userDetails.getUserId(),
                        startDate,
                        endDate,
                        limit
                )
        );
    }

    @GetMapping("/expense/trend")
    @Operation(summary = "Get expense trend")
    public ResponseEntity<ExpenseTrendResponse> getExpenseTrend(
            @AuthenticationPrincipal CustomUserDetails userDetails,

            @Parameter(description = "Trend unit (e.g. DAILY, WEEKLY, MONTHLY)")
            @RequestParam TrendUnit unit,

            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        // 기본값: 이번 달
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getExpenseTrend(
                        userDetails.getUserId(),
                        startDate,
                        endDate,
                        unit
                )
        );
    }
}
