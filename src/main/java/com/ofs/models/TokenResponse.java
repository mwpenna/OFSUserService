package com.ofs.models;

import lombok.Data;

@Data
public class TokenResponse {
    private String tokenType;
    private String token;

    public static TokenResponse generateBearerTokenResponse(String token) {
        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setToken(token);
        tokenResponse.setTokenType("bearer");

        return tokenResponse;
    }
}
