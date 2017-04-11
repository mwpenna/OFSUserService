package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.client.java.error.TemporaryFailureException;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultAsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultN1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofs.models.Company;
import com.ofs.models.User;
import com.ofs.server.errors.NotFoundException;
import com.ofs.server.errors.ServiceUnavailableException;
import com.ofs.server.utils.Dates;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;

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

        id = UUID.randomUUID();

        user = new User();
        user.setId(id);
        user.setPassword("somePassword");
        user.setActiveFlag(true);
        user.setEmailAddress("emailAddress");
        user.setFirstName("firstName");
        user.setUserName("demo");

        query = ParameterizedN1qlQuery.parameterized("", JsonObject.create());
        rows = new ArrayList<>();
        when(couchbaseFactory.getUserBucket()).thenReturn(bucket);
        when(ofsObjectMapper.writeValueAsString(anyString())).thenReturn(objectMapper.writeValueAsString(user));
    }

    @Test
    public void addUser_happyPath() throws com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException, JsonProcessingException {
        objectUnderTest.addUser(user);
        verify(couchbaseFactory.getUserBucket(), times(1)).insert(any());
    }

    @Test(expected = ServiceUnavailableException.class)
    public void getUserByUserName_shouldThrowServiceUnavailableExceptionWhenCouchbaseThrowsTempFailureException() throws Exception {
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenThrow(new TemporaryFailureException());
        objectUnderTest.getUserByUserName(user.getUserName());
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

    @Test(expected = ServiceUnavailableException.class)
    public void getUserByEmail_shouldThrowServiceUnavailableExceptionWhenCouchbaseThrowsTempFailureException() throws Exception {
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenThrow(new TemporaryFailureException());
        objectUnderTest.getUserByEmailAddress("123");
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

    @Test
    public void getUserByIdNUllId_shouldReturnEmptyOptional() {
        Optional<User> userOptional = objectUnderTest.getUserById(null);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void getUserById_happyPath() {
        when(bucket.get(anyString())).thenReturn(JsonDocument.create(id.toString(), generateUserJsonObject()));
        Optional<User> userOptional = objectUnderTest.getUserById(id.toString());
        assertTrue(userOptional.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void deleteUserByIdNullId_shouldThrowNPException() {
        objectUnderTest.deleteUserById(null);
    }

    @Test
    public void deleteUserById_shouldCallDelete() {
        objectUnderTest.deleteUserById("123");
        verify(bucket, times(1)).remove("123");
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserById_shouldThrowNotFoundExcpetionWhenDocumentDoesNotExists() {
        when(bucket.remove(anyString())).thenThrow(new DocumentDoesNotExistException());
        objectUnderTest.deleteUserById("123");
    }

    @Test(expected = ServiceUnavailableException.class)
    public void deleteUserById_shouldThrowServiceUnavailableExceptionWhenCouchbaseThrowsTempFailureException() {
        when(bucket.remove(anyString())).thenThrow(new TemporaryFailureException());
        objectUnderTest.deleteUserById("123");
    }

    private JsonDocument generateJsonDocument() {
        return JsonDocument.create("123", generateUserJsonObject());
    }

    private JsonObject generateUserJsonObject() {
        JsonObject jsonObject = JsonObject.create();
        jsonObject.put("id", id.toString());
        jsonObject.put("firstName", "firstName");
        jsonObject.put("lastName", "lastName");
        jsonObject.put("role", "ADMIN");
        jsonObject.put("userName", "someUserName");
        jsonObject.put("password", "somePassword");
        jsonObject.put("emailAddress", "emailAddress");
        jsonObject.put("activeFlag", true);

        return jsonObject;
    }

    private DefaultN1qlQueryResult generateSuccessResult() {
        DefaultN1qlQueryResult defaultN1qlQueryResult = new DefaultN1qlQueryResult(rows, null, null,
                null, null, null, true, null, null);

        return defaultN1qlQueryResult;
    }

}