package com.kms.seft203.api.auth;

import lombok.Data;

@Data
public class LogoutRequest {
    private String token;
    private String userId;
}
