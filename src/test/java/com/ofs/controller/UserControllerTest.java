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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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

    @Test
    public void getUser_happyPathSuccceeds() throws JsonProcessingException {
        when(userRepository.getUserById(any())).thenReturn(Optional.of(generateResponseUser()));
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl("users/id/"+id), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getToken_happyPathSucceeds() throws Exception {
        when(userRepository.getUserByUserName(anyString())).thenReturn(Optional.of(generateResponseUser()));

        HttpEntity<String> entity = new HttpEntity<>(generateAuthHeader());

        ResponseEntity<String> response = restTemplate.exchange(apiUrl("users/getToken"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteUser_happyPathSucceeds() {
        HttpEntity<String> entity = new HttpEntity<>(generateAuthHeader());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl("users/id/" + id), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void updateUser_happyPathSucceeds() throws Exception {
        when(userRepository.getUserById(anyString())).thenReturn(Optional.of(generateResponseUser()));
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String request = ofsObjectMapper.writeValueAsString(generateUpdateUser());

        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl("users/id/"+id), entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private User generateResponseUser() {
        User user = new User();

        user.setHref(URI.create(apiUrl("/users/id/"+id)));
        user.setFirstName("name");
        user.setLastName("lName");
        user.setRole(User.Role.ACCOUNT_MANAGER);
        user.setUserName("name.lName");
        user.setPassword("/x/sahoFY1NaZvnjfR5hb6vVb8azDOyRSv4gLZ5eFcMiGFEz4hFxtnYCPPsOhXHy");
        user.setEmailAddress("name.lName@place.com");
        user.setCompany(generateDefaultCompany());
        user.setActiveFlag(true);

        return user;
    }

    private User generateUpdateUser() {
        User user = new User();

        user.setFirstName("name2");
        user.setLastName("lName2");
        user.setRole(User.Role.ADMIN);
        user.setEmailAddress("name.lName2@place.com");
        user.setActiveFlag(false);

        return user;
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

    private  HttpHeaders generateAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Basic bXBlbm5hNjpwYXNzd29yZA==");
        return headers;
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