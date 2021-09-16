package com.kms.seft203.api.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
