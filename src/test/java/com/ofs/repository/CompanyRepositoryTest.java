package com.ofs.repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultAsyncN1qlQueryRow;
import com.couchbase.client.java.query.DefaultN1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofs.models.Company;
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
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CompanyRepositoryTest {

    @InjectMocks
    CompanyRepository objectUnderTest;

    @Mock
    ConnectionManager connectionManager;

    @Mock
    Cluster cluster;

    @Mock
    Bucket bucket;

    ObjectMapper objectMapper = new ObjectMapper();
    private ParameterizedN1qlQuery query;
    private List<AsyncN1qlQueryRow> rows;
    private Company company;
    private UUID id;

    @Before
    public void setup() {
        initMocks(this);
        company = new Company();
        company.setHref(URI.create("http://localhost:8082/company/id/"+ UUID.randomUUID()));
        company.setName("DemoCompany");
        company.setCreatedOn(Dates.now());
        company.setId(id);

        query = ParameterizedN1qlQuery.parameterized("", JsonObject.create());
        rows = new ArrayList<>();
        when(connectionManager.getCompanyBucket()).thenReturn(bucket);

        id = UUID.randomUUID();

    }

    @Test
    public void getCompanyByIdWithNullId_shouldReturnEmptyOptional() throws Exception {
        Optional<Company> companyOptional = objectUnderTest.getCompanyById(null);
        assertFalse(companyOptional.isPresent());
    }

    @Test
    public void getCompanyByIdShouldHandleNoSuchElementException_shouldReturnEmptyOptional() throws Exception {
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenThrow(new NoSuchElementException());
        Optional<Company> optional = objectUnderTest.getCompanyById(id.toString());
        assertFalse(optional.isPresent());
    }

    @Test
    public void getCompanyById_happyPath() throws Exception {
        String companyString = objectMapper.writeValueAsString(company);
        DefaultAsyncN1qlQueryRow row = new DefaultAsyncN1qlQueryRow(companyString.getBytes());
        rows.add(row);
        when(bucket.query(any(ParameterizedN1qlQuery.class))).thenReturn(generateSuccessResult());
        Optional<Company> optional = objectUnderTest.getCompanyById(id.toString());
        assertTrue(optional.isPresent());
    }

    private DefaultN1qlQueryResult generateSuccessResult() {
        DefaultN1qlQueryResult defaultN1qlQueryResult = new DefaultN1qlQueryResult(rows, null, null,
                null, null, null, true, null, null);

        return defaultN1qlQueryResult;
    }

}