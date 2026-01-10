package com.aespa.armageddon.core.api.auth.controller;

import com.aespa.armageddon.core.api.auth.dto.request.EmailVerificationConfirmRequest;
import com.aespa.armageddon.core.api.auth.dto.request.EmailVerificationRequest;
import com.aespa.armageddon.core.common.support.error.CoreException;
import com.aespa.armageddon.core.common.support.error.ErrorType;
import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.auth.repository.UserRepository;
import com.aespa.armageddon.core.domain.auth.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/email/verify")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;

    @PostMapping("/request")
    public ApiResult<?> request(@Valid @RequestBody EmailVerificationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CoreException(ErrorType.DUPLICATE_EMAIL);
        }

        emailVerificationService.requestVerification(request.getEmail());
        return ApiResult.success();
    }

    @PostMapping("/confirm")
    public ApiResult<?> confirm(@Valid @RequestBody EmailVerificationConfirmRequest request) {
        emailVerificationService.confirmVerification(request.getEmail(), request.getCode());
        return ApiResult.success();
    }
}
