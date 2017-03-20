package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultAsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultN1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofs.models.Company;
import com.ofs.models.User;
import com.ofs.server.utils.Dates;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserRepositoryTest {

    @InjectMocks
    UserRepository objectUnderTest;

    @Mock
    CouchbaseFactory couchbaseFactory;

    @Mock
    Cluster cluster;

    @Mock
    Bucket bucket;

    @Mock
    ObjectMapper ofsObjectMapper;

    ObjectMapper objectMapper = new ObjectMapper();
    private ParameterizedN1qlQuery query;
    private List<AsyncN1qlQueryRow> rows;
    private User user;
    private UUID id;

    @Before
    public void setup() throws JsonProcessingException {
        initMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword("somePassword");
        user.setActiveFlag(true);
        user.setEmailAddress("emailAddress");
        user.setFirstName("firstName");
        user.setUserName("demo");

        query = ParameterizedN1qlQuery.parameterized("", JsonObject.create());
        rows = new ArrayList<>();
        when(couchbaseFactory.getUserBucket()).thenReturn(bucket);
        when(ofsObjectMapper.writeValueAsString(anyString())).thenReturn(objectMapper.writeValueAsString(user));

        id = UUID.randomUUID();

    }

    @Test
    public void addUser_happyPath() throws com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException, JsonProcessingException {
        objectUnderTest.addUser(user);
        verify(couchbaseFactory.getUserBucket(), times(1)).insert(any());
    }

    @Test
    public void getUserByUserNameNullUserName_shouldReturnEmptyOptional() throws Exception {
        Optional<User> userOptional = objectUnderTest.getUserByUserName(null);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void getUserByUserNameShouldHandleNoSuchElementException_shouldReturnEmptyOptional() throws Exception {
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenThrow(new NoSuchElementException());
        Optional<User> optional = objectUnderTest.getUserByUserName(user.getUserName());
        assertFalse(optional.isPresent());
    }

    @Test
    public void getUserByUserName_happyPath() throws Exception {
        String userString = objectMapper.writeValueAsString(user);
        DefaultAsyncN1qlQueryRow row = new DefaultAsyncN1qlQueryRow(userString.getBytes());
        rows.add(row);
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenReturn(generateSuccessResult());
        Optional<User> optional = objectUnderTest.getUserByUserName(user.getUserName());
        assertTrue(optional.isPresent());
    }

    @Test
    public void getUserByEmailNullUserName_shouldReturnEmptyOptional() throws Exception {
        Optional<User> userOptional = objectUnderTest.getUserByEmailAddress(null);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void getUserByEmailShouldHandleNoSuchElementException_shouldReturnEmptyOptional() throws Exception {
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenThrow(new NoSuchElementException());
        Optional<User> optional = objectUnderTest.getUserByEmailAddress(user.getEmailAddress());
        assertFalse(optional.isPresent());
    }

    @Test
    public void getUserByEmail_happyPath() throws Exception {
        String userString = objectMapper.writeValueAsString(user);
        DefaultAsyncN1qlQueryRow row = new DefaultAsyncN1qlQueryRow(userString.getBytes());
        rows.add(row);
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenReturn(generateSuccessResult());
        Optional<User> optional = objectUnderTest.getUserByEmailAddress(user.getEmailAddress());
        assertTrue(optional.isPresent());
    }

    private DefaultN1qlQueryResult generateSuccessResult() {
        DefaultN1qlQueryResult defaultN1qlQueryResult = new DefaultN1qlQueryResult(rows, null, null,
                null, null, null, true, null, null);

        return defaultN1qlQueryResult;
    }

}