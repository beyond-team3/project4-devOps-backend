package com.aespa.armageddon.core.domain.transaction.query.dto;

import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class TransactionResponse {

    private Long id;                // Transaction 의 PK(수정/삭제용)
    private String title;           // 거래 제목
    private int amount;             // 거래 금액
    private TransactionType type;   // INCOME(수입) / EXPENDITURE(지출)

    @QueryProjection
    public TransactionResponse(Long id, String title, int amount, TransactionType type) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
    }
}
