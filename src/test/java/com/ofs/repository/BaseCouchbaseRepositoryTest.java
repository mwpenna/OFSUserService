package com.ofs.repository;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.TemporaryFailureException;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultAsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultN1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofs.models.User;
import com.ofs.server.config.JacksonConfiguration;
import com.ofs.server.errors.ServiceUnavailableException;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseCouchbaseRepositoryTest {

    @Mock
    CouchbaseFactory couchbaseFactory;

    @InjectMocks
    private UserRepository objectUnderTest;

    @Mock
    Cluster cluster;

    @Mock
    Bucket bucket;

    ObjectMapper objectMapper = new ObjectMapper();
    private ParameterizedN1qlQuery query;
    private List<AsyncN1qlQueryRow> rows;
    private User user;
    UUID id;

    @Before
    public void setup() {
        initMocks(this);
        id = UUID.randomUUID();
        user = new User();
        user.setId(id);
        user.setPassword("somePassword");
        user.setActiveFlag(true);
        user.setEmailAddress("emailAddress");
        user.setFirstName("firstName");
        user.setUserName("someUserName");

        query = ParameterizedN1qlQuery.parameterized("", JsonObject.create());
        rows = new ArrayList<>();
        when(couchbaseFactory.getUserBucket()).thenReturn(bucket);
    }

    @Test(expected = NullPointerException.class)
    public void queryForObjectByParametersWithNullQuery_shouldThrowNullPointerException() throws Exception {
        objectUnderTest.queryForObjectByParameters(null, bucket, User.class);
    }

    @Test(expected = NullPointerException.class)
    public void queryForObjectByParametersWithNullClazz_shouldThrowNullPointerException() throws Exception {
        objectUnderTest.queryForObjectByParameters(query, bucket, null);
    }

    @Test
    public void queryForObjectByParametersWithNoResults_shouldReturnEmptyOptional() throws Exception {
        when(couchbaseFactory.getUserBucket().query(eq(query))).thenReturn(generateSuccessResult());
        Optional<User> optional = objectUnderTest.queryForObjectByParameters(query, bucket, User.class);
        assertFalse(optional.isPresent());
    }

    @Test
    public void queryForObjectByParameters_happyPath() throws Exception {
        String userString = objectMapper.writeValueAsString(user);
        DefaultAsyncN1qlQueryRow row = new DefaultAsyncN1qlQueryRow(userString.getBytes());
        rows.add(row);
        when(couchbaseFactory.getUserBucket().query(eq(query))).thenReturn(generateSuccessResult());
        Optional<User> optional = objectUnderTest.queryForObjectByParameters(query, bucket, User.class);
        assertTrue(optional.isPresent());
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByParametersBackPressureException_shouldThrowServiceUnavalableException() throws Exception {
        when(couchbaseFactory.getUserBucket().query(eq(query))).thenThrow(new BackpressureException());
        objectUnderTest.queryForObjectByParameters(query, bucket, User.class);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByParametersRequestCancelledException_shouldThrowServiceUnavalableException() throws Exception {
        when(couchbaseFactory.getUserBucket().query(eq(query))).thenThrow(new RequestCancelledException());
        objectUnderTest.queryForObjectByParameters(query, bucket, User.class);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByParametersRuntimeException_shouldThrowServiceUnavalableException() throws Exception {
        when(couchbaseFactory.getUserBucket().query(eq(query))).thenThrow(new RuntimeException());
        objectUnderTest.queryForObjectByParameters(query, bucket, User.class);
    }

    @Test(expected = NullPointerException.class)
    public void queryForObjectByIdWithNullId_shouldThrowNullPointerException() {
        objectUnderTest.queryForObjectById(null, bucket, User.class);
    }

    @Test(expected = NullPointerException.class)
    public void queryForObjectByIdWithNullClass_shouldThrowNullPointerException() {
        objectUnderTest.queryForObjectById("123", bucket, null);
    }

    @Test(expected = NullPointerException.class)
    public void queryForObjectByIdWithNullBucket_shouldThrowNullPointerException() {
        objectUnderTest.queryForObjectById("123", null, User.class);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByIdBackpressureException_shouldThrowServiceUnavailableException() {
        when(bucket.get(anyString())).thenThrow(new BackpressureException());
        objectUnderTest.queryForObjectById("123", bucket, User.class);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByIdRequestCancelledException_shouldThrowServiceUnavailableException() {
        when(bucket.get(anyString())).thenThrow(new RequestCancelledException());
        objectUnderTest.queryForObjectById("123", bucket, User.class);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByIdTemporaryFailureException_shouldThrowServiceUnavailableException() {
        when(bucket.get(anyString())).thenThrow(new TemporaryFailureException());
        objectUnderTest.queryForObjectById("123", bucket, User.class);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void queryForObjectByIdRuntimeException_shouldThrowServiceUnavailableException() {
        when(bucket.get(anyString())).thenThrow(new RuntimeException());
        objectUnderTest.queryForObjectById("123", bucket, User.class);
    }

    @Test
    public void queryForObjectByIdNullDocumentReturned_shouldReturnEmptyOptional() {
        when(bucket.get(anyString())).thenReturn(null);
        Optional<User> userOptional = objectUnderTest.queryForObjectById("123", bucket, User.class);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void queryForObjectByIdNullContentReturned_shouldREturnEmptyOptional() {
        when(bucket.get(anyString())).thenReturn(JsonDocument.create("123"));
        Optional<User> userOptional = objectUnderTest.queryForObjectById("123", bucket, User.class);
        assertFalse(userOptional.isPresent());
    }

    @Test
    public void queryForObjectById_shouldReturnOptionalWithUserPresent() {
        when(bucket.get(anyString())).thenReturn(JsonDocument.create(id.toString(), generateUserJsonObject()));
        Optional<User> userOptional = objectUnderTest.queryForObjectById(id.toString(), bucket, User.class);
        assertTrue(userOptional.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void updateObject_shouldThrowNPwhenIdNull() throws JsonProcessingException {
        objectUnderTest.update(null, bucket, user);
    }

    @Test(expected = NullPointerException.class)
    public void updateObject_shouldThrowNPwhenBucketNull() throws JsonProcessingException {
        objectUnderTest.update("123", null, user);
    }

    @Test(expected = NullPointerException.class)
    public void updateObject_shouldThrowNPwhenObjectNull() throws JsonProcessingException {
        objectUnderTest.update("123", bucket, null);
    }

    @Test(expected = NullPointerException.class)
    public void deleteWithNullId_shouldThrowNP() {
        objectUnderTest.delete(null, bucket);
    }

    @Test(expected = NullPointerException.class)
    public void deleteWithNullBucket_shouldThrowNP() {
        objectUnderTest.delete("123", null);
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