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
        this.setFirstName((String) map.get("firstName"));
        this.setLastName((String) map.get("lastName"));
        String role = (String) map.get("role");
        this.setRole(role != null ? Role.valueOf((String) map.get("role")) : null);
        Map companyMap = (Map) map.get("company");
        this.setCompany( companyMap != null ? Company.getCompany(companyMap) : null);
        this.setUserName((String) map.get("userName"));
        this.setPassword((String) map.get("password"));
        this.setEmailAddress((String) map.get("emailAddress"));
        this.setToken((String) map.get("token"));
        this.setTokenExpDate((ZonedDateTime) map.get("tokenExpDate"));
        this.setActiveFlag((Boolean) map.get("activeFlag"));
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

//    @JsonIgnore
//    public static User getUser(Map userMap) {
//        User user = new User();
//
//        user.setId(UUID.fromString((String)userMap.get("id")));
//        user.setFirstName((String) userMap.get("firstName"));
//        user.setLastName((String) userMap.get("lastName"));
//        user.setRole(Role.valueOf((String) userMap.get("role")));
//        this.setCompany();
//        user.setUserName((String) userMap.get("userName"));
//        user.setPassword((String) userMap.get("password"));
//        user.setEmailAddress((String) userMap.get("emailAddress"));
//        user.setToken((String) userMap.get("token"));
//        user.setTokenExpDate((ZonedDateTime) userMap.get("tokenExpDate"));
//        user.setActiveFlag((Boolean) userMap.get("activeFlag"));
//
//        return user;
//    }
}
