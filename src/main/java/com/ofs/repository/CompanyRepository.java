package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;

import com.ofs.models.Company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class CompanyRepository {

    @Value("${clusterHostName}")
    private String clusterHostName;

    @Value("${clusterPassword}")
    private String clusterPassword;

    private final String COMPANY_BUCKET = "company";

    ObjectMapper objectMapper = new ObjectMapper();

    public @Bean
    Cluster cluster() {
        return CouchbaseCluster.create(clusterHostName);
    }

    public @Bean
    Bucket bucket() {
        return cluster().openBucket(COMPANY_BUCKET, clusterPassword);
    }

    public Optional<Company> getCompanyById(String id) {
        if(id == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(queryGetById(id));
        }
        catch (NoSuchElementException e) {
            log.warn("Company id {} does not exists", id, e);
            return Optional.empty();
        }
    }

    private String generateGetByIdQuery() {
        return "SELECT `" + bucket().name() + "`.* FROM `" + bucket().name() + "` where id = $id";
    }

    private JsonObject generateGetByIdParameters(String id) {
        return JsonObject.create().put("$id", id);
    }

    private Company queryGetById(String id) {
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(generateGetByIdQuery(), generateGetByIdParameters(id));
        Map companyMap =  bucket().async().query(query)
                .flatMap(AsyncN1qlQueryResult::rows)
                .map(result -> result.value().toMap())
                .timeout(10, TimeUnit.SECONDS)
                .toBlocking()
                .single();

        return Company.getCompany(companyMap);
    }
}
