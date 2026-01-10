package com.aespa.armageddon.core.domain.transaction.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_transaction")
@NoArgsConstructor
@Getter
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private Long userNo;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 255)
    private String memo;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private Category category;

    public Transaction(
            Long userNo,
            String title,
            String memo,
            int amount,
            LocalDate date,
            TransactionType type,
            Category category
    ) {
        this.userNo = userNo;
        this.title = title;
        this.memo = memo;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.category = category;
    }
}
