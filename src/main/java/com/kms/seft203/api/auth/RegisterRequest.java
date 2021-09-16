package com.kms.seft203.api.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
}
