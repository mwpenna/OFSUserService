package com.ofs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ofs.server.filter.views.SystemAdmin;
import com.ofs.server.model.BaseOFSEntity;
import com.ofs.server.model.OFSEntity;
import com.ofs.server.utils.Dates;
import com.ofs.utils.StringUtils;

import lombok.Data;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class User implements OFSEntity {

    public enum Role {
        SYSTEM_ADMIN,
        ADMIN,
        ACCOUNT_MANAGER,
        WAREHOUSE,
        CUSTOMER
    }

    public User() {
    }

    public User(URI href) {
        this();
        this.href = href;
        this.createdOn = Dates.now();
        this.id = UUID.fromString(this.getIdFromHref());
    }

    public  User(Map map) {
        String href = (String)map.get("href");
        this.setHref(href != null ? URI.create(href) : null);
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
        String tokenExpDate = (String) map.get("tokenExpDate");
        this.setTokenExpDate(tokenExpDate != null ? ZonedDateTime.parse(tokenExpDate) : null);
        this.setActiveFlag((Boolean) map.get("activeFlag"));
    }

    private UUID id;
    private String firstName;
    private String lastName;
    private Company company;
    private Role role;
    private String userName;
    private String emailAddress;
    private boolean activeFlag;
    private URI href;
    private ZonedDateTime createdOn;

    @JsonView(SystemAdmin.class)
    private ZonedDateTime tokenExpDate;

    @JsonView(SystemAdmin.class)
    private String token;

    @JsonView(SystemAdmin.class)
    private String password;

    @JsonIgnore
    public String getIdFromHref() {
        return StringUtils.getIdFromURI(getHref());
    }

    @JsonIgnore
    public static User getUser(Map userMap) {
        User user = new User();

        user.setId(UUID.fromString((String)userMap.get("id")));
        user.setFirstName((String) userMap.get("firstName"));
        user.setLastName((String) userMap.get("lastName"));
        String role = (String) userMap.get("role");
        user.setRole(role != null ? Role.valueOf((String) userMap.get("role")) : null);
        Map companyMap = (Map) userMap.get("company");
        user.setCompany( companyMap != null ? Company.getCompany(companyMap) : null);
        user.setUserName((String) userMap.get("userName"));
        user.setPassword((String) userMap.get("password"));
        user.setEmailAddress((String) userMap.get("emailAddress"));
        user.setToken((String) userMap.get("token"));
        String tokenExpDate = (String) userMap.get("tokenExpDate");
        user.setTokenExpDate(tokenExpDate != null ? ZonedDateTime.parse(tokenExpDate) : null);
        user.setActiveFlag((Boolean) userMap.get("activeFlag"));

        return user;
    }
}
