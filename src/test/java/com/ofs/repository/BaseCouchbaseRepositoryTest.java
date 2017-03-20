package com.ofs.repository;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultAsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultN1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
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
import static org.mockito.Matchers.eq;
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

    @Before
    public void setup() {
        initMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword("somePassword");
        user.setActiveFlag(true);
        user.setEmailAddress("emailAddress");
        user.setFirstName("firstName");

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

    private DefaultN1qlQueryResult generateSuccessResult() {
        DefaultN1qlQueryResult defaultN1qlQueryResult = new DefaultN1qlQueryResult(rows, null, null,
                null, null, null, true, null, null);

        return defaultN1qlQueryResult;
    }
}