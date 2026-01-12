package com.aespa.armageddon.core.domain.goal.controller;

import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.goal.dto.request.CreateExpenseGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.request.CreateSavingGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.request.UpdateGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.response.GoalDetailResponse;
import com.aespa.armageddon.core.domain.goal.dto.response.GoalResponse;
import com.aespa.armageddon.core.domain.goal.service.GoalService;
import com.aespa.armageddon.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

        private final GoalService goalService;
        private final JwtTokenProvider jwtTokenProvider;

        private Long extractUserId(String authorization) {
                String token = authorization.substring(7); // "Bearer "
                return jwtTokenProvider.getUserIdFromJWT(token);
        }

        /**
         * 목표 전체 조회 (저축 + 지출)
         */
        @GetMapping
        public ApiResult<List<GoalResponse>> getGoals(
                        @RequestHeader("Authorization") String authorization) {

                Long userId = extractUserId(authorization);
                return ApiResult.success(goalService.getGoals(userId));
        }

        /**
         * 목표 세부정보 조회 (진행률, 예측 포함)
         */
        @GetMapping("/{goalId}")
        public ApiResult<GoalDetailResponse> getGoalDetail(
                        @RequestHeader("Authorization") String authorization,
                        @PathVariable Long goalId) {

                Long userId = extractUserId(authorization);
                return ApiResult.success(goalService.getGoalDetail(userId, goalId));
        }

        /**
         * 저축 목표 생성
         */
        @PostMapping("/saving")
        public ApiResult<?> createSavingGoal(
                        @RequestHeader("Authorization") String authorization,
                        @RequestBody CreateSavingGoalRequest request) {

                Long userId = extractUserId(authorization);
                goalService.createSavingGoal(userId, request);
                return ApiResult.success();
        }

        /**
         * 지출 목표 생성
         */
        @PostMapping("/expense")
        public ApiResult<?> createExpenseGoal(
                        @RequestHeader("Authorization") String authorization,
                        @RequestBody CreateExpenseGoalRequest request) {

                Long userId = extractUserId(authorization);
                goalService.createExpenseGoal(userId, request);
                return ApiResult.success();
        }

        /**
         * 목표 수정 (금액 / 기간)
         */
        @PutMapping("/{goalId}")
        public ApiResult<?> updateGoal(
                        @RequestHeader("Authorization") String authorization,
                        @PathVariable Long goalId,
                        @RequestBody UpdateGoalRequest request) {

                Long userId = extractUserId(authorization);
                goalService.updateGoal(userId, goalId, request);
                return ApiResult.success();
        }

        /**
         * 목표 삭제 (Soft Delete)
         */
        @DeleteMapping("/{goalId}")
        public ApiResult<?> deleteGoal(
                        @RequestHeader("Authorization") String authorization,
                        @PathVariable Long goalId) {

                Long userId = extractUserId(authorization);
                goalService.deleteGoal(userId, goalId);
                return ApiResult.success();
        }
}
