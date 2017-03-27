package com.ofs.models;

import lombok.Data;

@Data
public class BasicAuthUser {

    public BasicAuthUser() {

    }

    public BasicAuthUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    String userName;
    String password;

    public static BasicAuthUser basicAuthUserFactory(String userName, String password) {
        return new BasicAuthUser(userName, password);
    }
}
