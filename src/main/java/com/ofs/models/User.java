package com.ofs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ofs.server.model.BaseOFSEntity;
import com.ofs.utils.StringUtils;
import lombok.Data;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;
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

    public  User(Map map) {
        this.setId(UUID.fromString((String)map.get("id")));
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

    @JsonIgnore
    public String getIdFromHref() {
        return StringUtils.getIdFromURI(getHref());
    }

    @JsonIgnore
    public static User getUser(Map userMap) {
        User user = new User();

        user.setId(UUID.fromString((String)userMap.get("id")));

        return user;
    }
}
