package com.aespa.armageddon.core.domain.goal.controller;

import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.goal.dto.*;
import com.aespa.armageddon.core.domain.goal.dto.request.CreateExpenseGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.request.CreateSavingGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.request.UpdateGoalRequest;
import com.aespa.armageddon.core.domain.goal.dto.response.GoalDetailResponse;
import com.aespa.armageddon.core.domain.goal.dto.response.GoalResponse;
import com.aespa.armageddon.core.domain.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    /**
     * 목표 전체 조회 (저축 + 지출)
     */
    @GetMapping
    public ApiResult<List<GoalResponse>> getGoals(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return ApiResult.success(
                goalService.getGoals(userId)
        );
    }

    /**
     * 목표 단건 조회 (진행률, 예측 포함)
     */
    @GetMapping("/{goalId}")
    public ApiResult<GoalDetailResponse> getGoalDetail(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long goalId
    ) {
        return ApiResult.success(
                goalService.getGoalDetail(userId, goalId)
        );
    }

    /**
     * 저축 목표 생성
     */
    @PostMapping("/saving")
    public ApiResult<?> createSavingGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody CreateSavingGoalRequest request
    ) {
        goalService.createSavingGoal(userId, request);
        return ApiResult.success();
    }

    /**
     * 지출 목표 생성
     */
    @PostMapping("/expense")
    public ApiResult<?> createExpenseGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody CreateExpenseGoalRequest request
    ) {
        goalService.createExpenseGoal(userId, request);
        return ApiResult.success();
    }

    /**
     * 목표 수정 (금액 / 기간)
     */
    @PutMapping("/{goalId}")
    public ApiResult<?> updateGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long goalId,
            @RequestBody UpdateGoalRequest request
    ) {
        goalService.updateGoal(userId, goalId, request);
        return ApiResult.success();
    }

    /**
     * 목표 삭제 (Soft Delete)
     */
    @DeleteMapping("/{goalId}")
    public ApiResult<?> deleteGoal(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long goalId
    ) {
        goalService.deleteGoal(userId, goalId);
        return ApiResult.success();
    }
}
