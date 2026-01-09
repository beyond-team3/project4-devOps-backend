package com.aespa.armageddon.core.domain.transaction.query.repository;


import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Category;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Transaction;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionResponse;
import com.aespa.armageddon.core.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryDslConfig.class, TransactionQueryRepository.class})
class TransactionQueryRepositoryTest {

    @Autowired
    TransactionQueryRepository transactionQueryRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("유저는 본인의 특정 날짜 내역만 정확히 조회할 수 있다.")
    void findDailyListTest() {

        // given (준비)
        Long userNo = 1L;
        LocalDate today = LocalDate.of(2026, 1 , 9);

        Transaction t1 = createTransaction(userNo, today, "스타벅스", 6500, TransactionType.EXPENSE);
        Transaction t2 = createTransaction(userNo, today.minusDays(1), "편의점", 3000, TransactionType.EXPENSE);
        Transaction t3 = createTransaction(2L, today, "월급", 5000000, TransactionType.INCOME);

        em.persist(t1);
        em.persist(t2);
        em.persist(t3);

        em.flush();
        em.clear();

        // when (실행)
        List<TransactionResponse> result = transactionQueryRepository.findDailyList(userNo, today);

        // then (확인)
        assertThat(result).hasSize(1);

        /* DTO -> id, Entity -> PK 잘 매핑 되었는지 확인 */
        TransactionResponse response = result.get(0);
        assertThat(response.getTitle()).isEqualTo("스타벅스");
        assertThat(response.getAmount()).isEqualTo(6500);
        assertThat(response.getType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(response.getId()).isNotNull();

    }

    private Transaction createTransaction(Long userNo, LocalDate date, String title, int amount, TransactionType type) {

        return new Transaction(
                userNo,
                title,
                "메모 테스트",
                amount,
                date,
                type,
                Category.FOOD
        );

    }
}