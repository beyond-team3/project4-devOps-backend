package com.aespa.armageddon.core.domain.auth.repository;

import com.aespa.armageddon.core.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByLoginId(String loginId);
}
