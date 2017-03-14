package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.ofs.models.Company;
import com.ofs.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class UserRepository {

    @Value("${clusterHostName}")
    private String clusterHostName;

    @Value("${clusterPassword}")
    private String clusterPassword;

    private final String USER_BUCKET = "users";

    ObjectMapper objectMapper = new ObjectMapper();

    public @Bean
    Cluster cluster() {
        return CouchbaseCluster.create(clusterHostName);
    }

    public @Bean
    Bucket bucket() {
        return cluster().openBucket(USER_BUCKET, clusterPassword);
    }

    public void addUser(User user) throws JsonProcessingException {
        JsonObject jsonObject = JsonObject.fromJson(objectMapper.writeValueAsString(user));
        JsonDocument jsonDocument = JsonDocument.create(user.getId().toString(), jsonObject);
        bucket().insert(jsonDocument);
    }

    public Optional<User> getUserById(String id) {
        return Optional.empty();
    }

    public Optional<User> getUserByUserName(String username) {
        try {
            return Optional.of(queryGetByUserName(username));
        }
        catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByEmailAddress(String emailAddress) {
        try {
            return Optional.of(queryGetByEmailAddressName(emailAddress));
        }
        catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private String generateGetByUserNameQuery(String userName) {
        return "SELECT `" + bucket().name() + "`.* FROM `" + bucket().name() + "` where userName = \"$userName\"";
    }

    private JsonObject generateGetByUserNameParameters(String userName) {
        return JsonObject.create().put("userName", userName);
    }

    private User queryGetByUserName(String userName) {
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(
                generateGetByUserNameQuery(userName), generateGetByUserNameParameters(userName));

        return (User) bucket().async().query(query)
                .flatMap(AsyncN1qlQueryResult::rows)
                .filter(result -> result.value().isEmpty())
                .map(result -> result.value().toMap())
                .timeout(10, TimeUnit.SECONDS)
                .toBlocking()
                .single();
    }

    private String generateGetByEmailAddressQuery(String emailAddress) {
        return "SELECT `" + bucket().name() + "`.* FROM `" + bucket().name() + "` where emailAddress = \"emailAddress\"";
    }

    private JsonObject generateGetByEmailAddressParameters(String emailAddress) {
        return JsonObject.create().put("emailAddress", emailAddress);
    }

    private User queryGetByEmailAddressName(String emailAddress) {
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(
                generateGetByEmailAddressQuery(emailAddress), generateGetByEmailAddressParameters(emailAddress));

        return (User) bucket().async().query(query)
                .flatMap(AsyncN1qlQueryResult::rows)
                .filter(result -> result.value().isEmpty())
                .map(result -> result.value().toMap())
                .timeout(10, TimeUnit.SECONDS)
                .toBlocking()
                .single();
    }

}
