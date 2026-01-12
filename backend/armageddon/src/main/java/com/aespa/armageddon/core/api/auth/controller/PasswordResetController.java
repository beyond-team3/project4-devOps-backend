package com.aespa.armageddon.core.api.auth.controller;

import com.aespa.armageddon.core.api.auth.dto.request.PasswordResetConfirmRequest;
import com.aespa.armageddon.core.api.auth.dto.request.PasswordResetRequest;
import com.aespa.armageddon.core.common.support.response.ApiResult;
import com.aespa.armageddon.core.domain.auth.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/password/reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ApiResult<?> requestReset(@Valid @RequestBody PasswordResetRequest req) {
        passwordResetService.requestReset(req);
        return ApiResult.success();
    }

    @PostMapping("/confirm")
    public ApiResult<?> confirmReset(@Valid @RequestBody PasswordResetConfirmRequest req) {
        passwordResetService.confirmReset(req);
        return ApiResult.success();
    }
}
