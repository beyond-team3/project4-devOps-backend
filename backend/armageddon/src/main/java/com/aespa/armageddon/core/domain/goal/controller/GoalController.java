package com.aespa.armageddon.core.domain.goal.controller;

import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.auth.security.CustomUserDetails;
import com.aespa.armageddon.core.domain.goal.dto.request.CreateExpenseGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.request.CreateSavingGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.request.UpdateGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.response.GoalDetailResponse;
import com.aespa.armageddon.core.domain.goal.dto.response.GoalResponse;
import com.aespa.armageddon.core.domain.goal.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "Goal management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class GoalController {

        private final GoalService goalService;

        /**
         * 목표 전체 조회 (저축 + 지출)
         */
        @GetMapping
        @Operation(summary = "Get all goals")
        public ApiResult<List<GoalResponse>> getGoals(
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

                return ApiResult.success(goalService.getGoals(userDetails.getUserId()));
        }

        /**
         * 목표 세부정보 조회 (진행률, 예측 포함)
         */
        @GetMapping("/{goalId}")
        @Operation(summary = "Get goal details")
        public ApiResult<GoalDetailResponse> getGoalDetail(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long goalId) {

                return ApiResult.success(goalService.getGoalDetail(userDetails.getUserId(), goalId));
        }

        /**
         * 저축 목표 생성
         */
        @PostMapping("/saving")
        @Operation(summary = "Create saving goal")
        public ApiResult<?> createSavingGoal(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestBody CreateSavingGoalRequest request) {

                goalService.createSavingGoal(userDetails.getUserId(), request);
                return ApiResult.success();
        }

        /**
         * 지출 목표 생성
         */
        @PostMapping("/expense")
        @Operation(summary = "Create expense goal")
        public ApiResult<?> createExpenseGoal(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestBody CreateExpenseGoalRequest request) {

                goalService.createExpenseGoal(userDetails.getUserId(), request);
                return ApiResult.success();
        }

        /**
         * 목표 수정 (금액 / 기간)
         */
        @PutMapping("/{goalId}")
        @Operation(summary = "Update goal")
        public ApiResult<?> updateGoal(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long goalId,
                        @RequestBody UpdateGoalRequest request) {

                goalService.updateGoal(userDetails.getUserId(), goalId, request);
                return ApiResult.success();
        }

        /**
         * 목표 삭제 (Soft Delete)
         */
        @DeleteMapping("/{goalId}")
        @Operation(summary = "Delete goal")
        public ApiResult<?> deleteGoal(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long goalId) {

                goalService.deleteGoal(userDetails.getUserId(), goalId);
                return ApiResult.success();
        }
}
