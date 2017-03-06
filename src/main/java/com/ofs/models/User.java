package com.ofs.models;

import com.ofs.server.model.BaseOFSEntity;
import lombok.Data;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class User extends BaseOFSEntity {

    public enum Role {
        ADMIN,
        ACCOUNT_MANAGER,
        WAREHOUSE,
        CUSTOMER
    }

    public User() {
    }

    public User(URI href) {
        super(href);
    }

    private UUID id;
    private String firstName;
    private String lastName;
    private Company company;
    private Role role;
    private String userName;
    private String password;
    private String emailAddress;
    private String token;
    private ZonedDateTime tokenExpDate;
    private boolean activeFlag;
}
