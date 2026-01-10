package com.aespa.armageddon.core.domain.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String loginId;

    private String token;

    private Date expiryDate;

    @Builder
    public RefreshToken(String loginId, String token, Date expiryDate) {
        this.loginId = loginId;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
