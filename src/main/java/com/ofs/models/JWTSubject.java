package com.ofs.models;

import lombok.Data;

import java.net.URI;

@Data
public class JWTSubject {
    URI userHref;
    URI companyHref;
    String firstName;
    String lastName;
    User.Role role;
    String userName;
    String userEmailAddress;

    public static JWTSubject fromUser(User user) {
        JWTSubject jwtSubject = new JWTSubject();

        jwtSubject.setUserName(user.getUserName());
        jwtSubject.setCompanyHref(user.getCompany().getHref());
        jwtSubject.setFirstName(user.getFirstName());
        jwtSubject.setLastName(user.getLastName());
        jwtSubject.setRole(user.getRole());
        jwtSubject.setUserEmailAddress(user.getEmailAddress());
        jwtSubject.setUserHref(user.getHref());

        return jwtSubject;
    }
}
