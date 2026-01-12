package com.aespa.armageddon.core.domain.cashflow.controller;

import com.aespa.armageddon.core.common.support.error.CoreException;
import com.aespa.armageddon.core.common.support.error.ErrorType;
import com.aespa.armageddon.core.domain.cashflow.dto.SummaryStatisticsResponse;
import com.aespa.armageddon.core.domain.cashflow.service.StatisticsService;
import com.aespa.armageddon.infra.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

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
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }

        String token = authorization.substring(7);
        Long userNo = jwtTokenProvider.getUserIdFromJWT(token);

        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        return ResponseEntity.ok(
                statisticsService.getSummary(userNo, startDate, endDate)
        );
    }
}
