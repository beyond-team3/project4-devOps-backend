package com.aespa.armageddon.core.domain.goal.domain;

public enum GoalStatus {
    ACTIVE,     // 진행 중
    COMPLETED,  // 종료
    DELETED     // 삭제 (Soft Delete)
}