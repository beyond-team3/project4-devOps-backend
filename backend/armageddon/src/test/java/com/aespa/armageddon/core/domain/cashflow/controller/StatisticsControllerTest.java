package com.aespa.armageddon.core.domain.cashflow.controller;

import com.aespa.armageddon.core.domain.cashflow.dto.*;
import com.aespa.armageddon.core.domain.cashflow.service.StatisticsService;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Category;
import com.aespa.armageddon.core.domain.auth.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatisticsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SuppressWarnings("deprecation")
    @MockBean
    StatisticsService statisticsService;

    private static final Long USER_ID = 1L;
    private static final String LOGIN_ID = "testUser";

    private Authentication authentication() {
        CustomUserDetails userDetails = new CustomUserDetails(USER_ID, LOGIN_ID, "password", null);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication());
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("요약 통계 조회 - 성공")
    void getSummaryStatistics() throws Exception {
        // given
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        SummaryStatisticsResponse response = new SummaryStatisticsResponse(
                10000L, // totalIncome
                5000L, // totalExpense
                5000L, // netProfit
                166L // averageDailyExpense
        );

        given(statisticsService.getSummary(eq(USER_ID), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/statistics/summary")
                .param("startDate", start.toString())
                .param("endDate", end.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(10000))
                .andExpect(jsonPath("$.totalExpense").value(5000))
                .andExpect(jsonPath("$.netProfit").value(5000))
                .andExpect(jsonPath("$.averageDailyExpense").value(166));
    }

    @Test
    @DisplayName("카테고리별 지출 비율 조회 - 성공")
    void getCategoryExpenseStatistics() throws Exception {
        // given
        List<CategoryExpenseRatio> response = List.of(
                new CategoryExpenseRatio(Category.FOOD, 5000L, 50.0),
                new CategoryExpenseRatio(Category.TRANSPORT, 5000L, 50.0));

        given(statisticsService.getCategoryExpenseWithRatio(eq(USER_ID), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/statistics/expense/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].category").value("FOOD"))
                .andExpect(jsonPath("$[0].ratio").value(50.0));
    }

    @Test
    @DisplayName("상위 지출 항목 조회 - 성공")
    void getTopExpenseItems() throws Exception {
        // given
        TopExpenseItemResponse item = new TopExpenseItemResponse(
                1L, "Expensive Item", 50000,
                Category.SHOPPING, LocalDate.of(2024, 1, 15)
        );

        given(statisticsService.getTopExpenseItems(
                eq(USER_ID),
                any(LocalDate.class),
                any(LocalDate.class),
                anyInt()
        )).willReturn(List.of(item));

        // when & then
        mockMvc.perform(get("/api/statistics/expense/top")
                        .param("limit", "5")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value(1L))
                .andExpect(jsonPath("$[0].amount").value(50000));
    }


    @Test
    @DisplayName("지출 추이 조회 - 성공")
    void getExpenseTrend() throws Exception {
        // given
        TrendUnit unit = TrendUnit.DAY;

        ExpenseTrendResponse response = new ExpenseTrendResponse(
                unit,
                List.of(new ExpenseTrendPoint("2024-01-01", 1000L)));

        given(statisticsService.getExpenseTrend(eq(USER_ID), any(LocalDate.class), any(LocalDate.class), eq(unit)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/statistics/expense/trend")
                .param("unit", "DAY")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unit").value("DAY"))
                .andExpect(jsonPath("$.data[0].amount").value(1000));
    }
}
