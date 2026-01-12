package com.aespa.armageddon.core.domain.goal.service;

import com.aespa.armageddon.core.domain.goal.domain.*;
import com.aespa.armageddon.core.domain.goal.dto.request.*;
import com.aespa.armageddon.core.domain.goal.dto.response.*;
import com.aespa.armageddon.core.domain.goal.repository.GoalRepository;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Category;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.transaction.query.service.TransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final TransactionQueryService transactionQueryService;

    /* ===================== 조회 ===================== */

    @Transactional(readOnly = true)
    public List<GoalResponse> getGoals(Long userId) {
        return goalRepository.findByUserIdAndStatusNot(userId, GoalStatus.DELETED)
                .stream()
                .map(this::toGoalResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GoalDetailResponse getGoalDetail(Long userId, Long goalId) {
        Goal goal = findGoal(userId, goalId);

        int currentAmount = getCurrentAmount(goal);
        int progressRate = calculateRate(currentAmount, goal.getTargetAmount());
        Integer expectedAmount = calculateExpectedAmount(goal, currentAmount);

        return new GoalDetailResponse(
                goal.getGoalId(),
                goal.getGoalType(),
                goal.getTargetAmount(),
                currentAmount,
                progressRate,
                expectedAmount,
                createStatusMessage(goal, currentAmount, expectedAmount),
                goal.getStartDate(),
                goal.getEndDate(),
                goal.getStatus());
    }

    /* ===================== 생성 ===================== */

    public void createSavingGoal(Long userId, CreateSavingGoalRequest request) {
        Goal goal = Goal.createSavingGoal(
                userId,
                request.title(),
                request.targetAmount(),
                request.startDate(),
                request.endDate());
        goalRepository.save(goal);
    }

    public void createExpenseGoal(Long userId, CreateExpenseGoalRequest request) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        Goal goal = Goal.createExpenseGoal(
                userId,
                request.category(),
                request.title(),
                request.targetAmount(),
                start,
                end);
        goalRepository.save(goal);
    }

    /* ===================== 수정 / 삭제 ===================== */

    public void updateGoal(Long userId, Long goalId, UpdateGoalRequest request) {
        Goal goal = findGoal(userId, goalId);
        goal.updateTarget(request.targetAmount(), request.endDate());
    }

    public void deleteGoal(Long userId, Long goalId) {
        Goal goal = findGoal(userId, goalId);
        goal.delete();
    }

    /* ===================== 내부 로직 ===================== */

    private Goal findGoal(Long userId, Long goalId) {
        return goalRepository.findByGoalIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("목표를 찾을 수 없습니다."));
    }

    private int getCurrentAmount(Goal goal) {
        Category category = goal.getGoalType() == GoalType.SAVING
                ? Category.SAVING
                : Category.valueOf(goal.getExpenseCategory().name());

        Long sum = transactionQueryService.getTransactionSum(
                goal.getUserId(),
                category,
                TransactionType.EXPENSE,
                goal.getStartDate(),
                goal.getEndDate());
        return sum.intValue();
    }

    private int calculateRate(int current, int target) {
        if (target == 0)
            return 0;
        return Math.min((current * 100) / target, 100);
    }

    private Integer calculateExpectedAmount(Goal goal, int currentAmount) {
        if (goal.getGoalType() != GoalType.SAVING)
            return null;

        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate()) + 1;
        long passedDays = ChronoUnit.DAYS.between(goal.getStartDate(), LocalDate.now()) + 1;

        if (passedDays <= 0)
            return null;

        int dailyAverage = (int) (currentAmount / passedDays);
        return dailyAverage * (int) totalDays;
    }

    private String createStatusMessage(Goal goal, int current, Integer expected) {
        if (goal.getGoalType() == GoalType.EXPENSE) {
            int remaining = goal.getTargetAmount() - current;
            if (remaining < 0)
                return "이번 달 목표를 초과했어요";

            double rate = (double) current / goal.getTargetAmount();
            if (rate >= 0.8)
                return "목표 금액에 가까워지고 있어요. 주의하세요!";

            return "아직 여유가 있어요";
        }

        if (expected == null)
            return "조금 더 지켜볼게요";

        int diff = expected - goal.getTargetAmount();
        if (diff > 0)
            return "현재 속도라면 목표보다 더 모을 수 있어요";
        if (diff < 0)
            return "현재 속도라면 목표 금액에 조금 못 미칠 수 있어요";
        return "현재 페이스로 목표 달성이 가능해요";
    }

    private GoalResponse toGoalResponse(Goal goal) {
        int current = getCurrentAmount(goal);
        int rate = calculateRate(current, goal.getTargetAmount());

        return new GoalResponse(
                goal.getGoalId(),
                goal.getGoalType(),
                goal.getTitle(),
                goal.getTargetAmount(),
                rate,
                goal.getStatus());
    }
}
