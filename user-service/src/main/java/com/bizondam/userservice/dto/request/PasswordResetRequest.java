package com.bizondam.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    private String newPassword;

    public PasswordResetRequest(String newPassword) {
        this.newPassword = newPassword;
    }
}
