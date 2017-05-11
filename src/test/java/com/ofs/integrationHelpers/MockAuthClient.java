package com.ofs.integrationHelpers;

import com.ofs.server.client.AuthenticationClient;
import com.ofs.server.model.JWTSubject;
import org.springframework.web.bind.annotation.RequestHeader;

public class MockAuthClient implements AuthenticationClient {

    private JWTSubject subject;

    @Override
    public JWTSubject authenticate(@RequestHeader("Authorization") String s) {
        return subject;
    }

    public void setSubject(JWTSubject subject) {
        this.subject = subject;
    }
}
