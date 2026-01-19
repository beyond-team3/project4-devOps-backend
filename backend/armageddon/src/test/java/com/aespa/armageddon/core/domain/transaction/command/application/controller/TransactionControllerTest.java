package com.aespa.armageddon.core.domain.transaction.command.application.controller;

import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionEditRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.dto.request.TransactionWriteRequest;
import com.aespa.armageddon.core.domain.transaction.command.application.service.TransactionService;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Category;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.auth.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false) // Security Filter 비활성화 (순수 컨트롤러 로직 검증)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation")
    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long USER_NO = 1L;
    private static final String LOGIN_ID = "testuser";

    private Authentication authentication() {
        CustomUserDetails userDetails = new CustomUserDetails(USER_NO, LOGIN_ID, "password", null);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication());
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("거래 생성 성공")
    void writeTransaction_Success() throws Exception {
        // given
        TransactionWriteRequest request = new TransactionWriteRequest(
                "점심", "메모", 5000, LocalDate.now(), TransactionType.EXPENSE, Category.FOOD);

        // when & then
        mockMvc.perform(post("/transaction/write")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(transactionService).writeTransaction(eq(USER_NO), any(TransactionWriteRequest.class));
    }

    @Test
    @DisplayName("거래 수정 성공")
    void editTransaction_Success() throws Exception {
        // given
        Long transactionId = 100L;
        TransactionEditRequest request = new TransactionEditRequest(
                "저녁", "메모", 12000, LocalDate.now(), TransactionType.EXPENSE, Category.FOOD);

        // when & then
        mockMvc.perform(put("/transaction/edit/{transactionId}", transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(transactionService).editTransaction(eq(USER_NO), eq(transactionId), any(TransactionEditRequest.class));
    }

    @Test
    @DisplayName("거래 삭제 성공")
    void deleteTransaction_Success() throws Exception {
        // given
        Long transactionId = 100L;

        // when & then
        mockMvc.perform(delete("/transaction/delete/{transactionId}", transactionId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(transactionService).deleteTransaction(USER_NO, transactionId);
    }
}
