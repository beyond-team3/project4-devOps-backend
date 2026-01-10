package com.aespa.armageddon.core.domain.auth.service;


import com.aespa.armageddon.core.api.auth.dto.request.PasswordResetConfirmRequest;
import com.aespa.armageddon.core.api.auth.dto.request.PasswordResetRequest;
import com.aespa.armageddon.core.common.support.error.CoreException;
import com.aespa.armageddon.core.common.support.error.ErrorType;
import com.aespa.armageddon.core.domain.auth.entity.PasswordResetToken;
import com.aespa.armageddon.core.domain.auth.entity.User;
import com.aespa.armageddon.core.domain.auth.repository.PasswordResetTokenRepository;
import com.aespa.armageddon.core.domain.auth.repository.RefreshTokenRepository;
import com.aespa.armageddon.core.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final Duration TTL = Duration.ofMinutes(10);

    @Transactional
    public void requestReset(PasswordResetRequest req) {
        if (req == null) {
            return;
        }

        String loginId = trimToNull(req.getLoginId());
        String email = trimToNull(req.getEmail());
        if (loginId == null || email == null) {
            return;
        }

        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optUser.isEmpty()) {
            return;
        }

        User user = optUser.get();
        String savedEmail = trimToNull(user.getEmail());
        if (savedEmail == null || !savedEmail.equalsIgnoreCase(email)) {
            return;
        }

        String code = generate6DigitCode();
        String codeHash = sha256(code);

        PasswordResetToken token = PasswordResetToken.create(
                user.getId(), codeHash, TTL
        );
        tokenRepository.save(token);

        mailService.sendPasswordResetCode(user.getEmail(), code);
    }

    @Transactional
    public void confirmReset(PasswordResetConfirmRequest req) {
        if (req == null) {
            throw new CoreException(ErrorType.INVALID_INPUT_VALUE);
        }

        String loginId = trimToNull(req.getLoginId());
        String code = trimToNull(req.getCode());
        String newPassword = trimToNull(req.getNewPassword());

        if (loginId == null || code == null || newPassword == null) {
            throw new CoreException(ErrorType.INVALID_INPUT_VALUE);
        }

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CoreException(ErrorType.INVALID_PASSWORD_RESET_CODE));

        PasswordResetToken token = tokenRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new CoreException(ErrorType.INVALID_PASSWORD_RESET_CODE));

        if (token.isUsed() || token.isExpired()) {
            throw new CoreException(ErrorType.INVALID_PASSWORD_RESET_CODE);
        }

        String inputHash = sha256(code);
        if (!token.getCodeHash().equals(inputHash)) {
            throw new CoreException(ErrorType.INVALID_PASSWORD_RESET_CODE);
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        token.markUsed();
        refreshTokenRepository.deleteByLoginId(user.getLoginId());
    }

    private String generate6DigitCode() {
        int n = ThreadLocalRandom.current().nextInt(0, 1_000_000);
        return String.format("%06d", n);
    }

    private String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
