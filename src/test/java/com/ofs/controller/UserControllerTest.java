package com.ofs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ofs.integrationHelpers.WebIntegrationTestbootstrap;
import com.ofs.models.Company;
import com.ofs.models.JWTSubject;
import com.ofs.models.TokenResponse;
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

import java.io.IOException;
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
    public void createUser_happyPathSucceeds() throws Exception {
        when(authenticationClient.authenticate(any())).thenReturn(generateJWTServerSubject());
        HttpHeaders headers = createHeaders("123");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String request = ofsObjectMapper.writeValueAsString(generateDefaultUser());

        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl("users"), entity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void getUser_happyPathSuccceeds() throws Exception {
        when(authenticationClient.authenticate(any())).thenReturn(generateJWTServerSubject());
        when(userRepository.getUserById(any())).thenReturn(Optional.of(generateResponseUser()));

        HttpHeaders headers = createHeaders("123");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl("users/id/"+id), HttpMethod.GET,entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getToken_happyPathSucceeds() throws Exception {
        when(userRepository.getUserByUserName(anyString())).thenReturn(Optional.of(generateResponseUser()));

        HttpEntity<String> entity = new HttpEntity<>(generateBasicAuthHeader());

        ResponseEntity<String> response = restTemplate.exchange(apiUrl("users/getToken"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteUser_happyPathSucceeds() {
        when(authenticationClient.authenticate(any())).thenReturn(generateJWTServerSubject());

        HttpHeaders headers = createHeaders("123");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl("users/id/" + id), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void updateUser_happyPathSucceeds() throws Exception {
        when(userRepository.getUserById(anyString())).thenReturn(Optional.of(generateResponseUser()));
        when(authenticationClient.authenticate(any())).thenReturn(generateJWTServerSubject());
        
        HttpHeaders headers = createHeaders("123");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String request = ofsObjectMapper.writeValueAsString(generateUpdateUser());

        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl("users/id/"+id), entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void authenticateUser_happyPathSucceeds() throws IOException {
        when(userService.authenticate(anyString())).thenReturn(generateJWTSubject());
        HttpEntity<String> entity = new HttpEntity<>(generateBearerAuthHeader());

        ResponseEntity<String> response = restTemplate.exchange(apiUrl("users/authenticate"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private JWTSubject generateJWTSubject() {
        return JWTSubject.fromUser(generateResponseUser());
    }

    private com.ofs.server.model.JWTSubject generateJWTServerSubject() {
        com.ofs.server.model.JWTSubject subject = new com.ofs.server.model.JWTSubject();

        subject.setHref(URI.create(apiUrl("/users/id/"+id)));
        subject.setFirstName("name");
        subject.setLastName("lName");
        subject.setRole(com.ofs.server.model.JWTSubject.Role.SYSTEM_ADMIN);
        subject.setUserName("name.lName");
        subject.setEmailAddress("name.lName@place.com");
        subject.setCompanyHref(generateDefaultCompany().getHref());

        return subject;
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

    private  HttpHeaders generateBasicAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Basic bXBlbm5hNjpwYXNzd29yZA==");
        return headers;
    }

    private HttpHeaders generateBearerAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJKV1RTdWJqZWN0IiwiaWF0IjoxNDkxODM5NTgzLCJzdWIiOiJ7XCJ1c2VySHJlZlwiOlwiaHR0cDovL2xvY2FsaG9zdDo4MDgyL3VzZXJzL2lkLzJkZTI2NmQ1LTllMzctNGM5Zi04NWQ0LWI1NGI2ODdhZmU0OFwiLFwiY29tcGFueUhyZWZcIjpcImh0dHA6Ly9sb2NhbGhvc3Q6ODA4Mi9jb21wYW55L2lkLzVhMTIzNTA1LWJjYzEtNDQxNC04MzRiLWMxYzNhODI2Y2Y4ZlwiLFwiZmlyc3ROYW1lXCI6XCJmbmFtZVwiLFwibGFzdE5hbWVcIjpcImxuYW1lXCIsXCJyb2xlXCI6XCJBRE1JTlwiLFwidXNlck5hbWVcIjpcIk1hZ25lbWl0ZTEwMDdcIixcInVzZXJFbWFpbEFkZHJlc3NcIjpcIk1hZ25lbWl0ZTEwMDdAcG9rZW1vbi5jb21cIn0iLCJpc3MiOiJPRlNVc2VyU2VydmljZSJ9.ZpViQxS7UrRxKMTDeb5Ik8m8GlJCo8uv12pV0KBjDVk");
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