package com.ofs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ofs.integrationHelpers.WebIntegrationTestbootstrap;
import com.ofs.models.Company;
import com.ofs.models.User;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserControllerTest extends WebIntegrationTestbootstrap {

    private UUID id;
    private UUID companyId;

    @Before
    public void setup() {
        id = UUID.randomUUID();
        companyId = UUID.randomUUID();
    }

    @Test
    public void createUser_happyPathSucceeds() throws JsonProcessingException {
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String request = ofsObjectMapper.writeValueAsString(generateDefaultUser());

        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl("users"), entity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private UserRequest generateDefaultUser() {
        UserRequest user = new UserRequest();

        user.setFirstName("name");
        user.setLastName("lName");
        user.setRole(User.Role.ACCOUNT_MANAGER);
        user.setUserName("name.lName");
        user.setPassword("password");
        user.setEmailAddress("name.lName@place.com");
        user.setCompany(generateDefaultCompany());

        return user;
    }

    private Company generateDefaultCompany() {
        Company company = new Company();

        company.setHref(URI.create(apiUrl("company/id/" + companyId)));
        company.setName("demoCompany");

        return company;
    }

    @Data
    private class UserRequest {
        private UUID id;
        private String firstName;
        private String lastName;
        private Company company;
        private User.Role role;
        private String userName;
        private String password;
        private String emailAddress;
        private String token;
        private ZonedDateTime tokenExpDate;
    }

}