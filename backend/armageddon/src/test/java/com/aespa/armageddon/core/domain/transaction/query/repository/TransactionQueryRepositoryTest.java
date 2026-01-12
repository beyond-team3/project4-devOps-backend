package com.aespa.armageddon.core.domain.transaction.query.repository;


import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Category;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.Transaction;
import com.aespa.armageddon.core.domain.transaction.command.domain.aggregate.TransactionType;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionResponse;
import com.aespa.armageddon.core.domain.transaction.query.dto.TransactionSummaryResponse;
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

        Transaction t1 = createTransaction(userNo, today, "스타벅스", 6500, TransactionType.EXPENSE, Category.FOOD);
        Transaction t2 = createTransaction(userNo, today.minusDays(1), "편의점", 3000, TransactionType.EXPENSE, Category.FOOD);
        Transaction t3 = createTransaction(2L, today, "월급", 5000000, TransactionType.INCOME, null);

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

    private Transaction createTransaction(Long userNo, LocalDate date, String title, int amount, TransactionType type, Category category) {

        return new Transaction(
                userNo,
                title,
                "메모 테스트",
                amount,
                date,
                type,
                category
        );

    }

    @Test
    @DisplayName("월간 수입/지출/잔액 요약이 정확히 계산되어야 한다")
    void findMonthlySummaryTest() {
        // given (준비)
        Long userNo = 1L;
        int year = 2024;
        int month = 5; // 5월 데이터를 조회한다고 가정

        // 1. [포함 O] 5월 수입 (100,000원)
        Transaction t1 = createTransaction(userNo, LocalDate.of(year, month, 1),"월급", 100000, TransactionType.INCOME, null);

        // 2. [포함 O] 5월 지출 (30,000원)
        Transaction t2 = createTransaction(userNo, LocalDate.of(year, month, 15), "쇼핑", 30000, TransactionType.EXPENSE, Category.SHOPPING);

        // 3. [포함 O] 5월 지출 (10,000원) - 지출이 여러 건일 때 합산 잘 되는지
        Transaction t3 = createTransaction(userNo, LocalDate.of(year, month, 31), "식비", 10000, TransactionType.EXPENSE, Category.FOOD);

        // 4. [포함 X] 4월 데이터 (날짜 범위 밖)
        Transaction t4 = createTransaction(userNo, LocalDate.of(year, 4, 30), "지난달", 50000, TransactionType.INCOME, null);

        // 5. [포함 X] 다른 유저 데이터
        Transaction t5 = createTransaction(2L, LocalDate.of(year, month, 10), "남의돈", 999999, TransactionType.INCOME, null);

        em.persist(t1);
        em.persist(t2);
        em.persist(t3);
        em.persist(t4);
        em.persist(t5);

        // 영속성 컨텍스트 비우기 (DB에서 쿼리로 진짜 계산해오는지 확인 위함)
        em.flush();
        em.clear();

        // when (실행)
        TransactionSummaryResponse result = transactionQueryRepository.findMonthlySummary(userNo, year, month);

        // then (검증)
        // 예상 수입: 100,000 (t1)
        assertThat(result.getTotalIncome()).isEqualTo(100000L);

        // 예상 지출: 30,000 (t2) + 10,000 (t3) = 40,000
        assertThat(result.getTotalExpense()).isEqualTo(40000L);

        // 예상 잔액: 100,000 - 40,000 = 60,000
        assertThat(result.getBalance()).isEqualTo(60000L);

        System.out.println("테스트 성공 결과: " + result);
    }
}