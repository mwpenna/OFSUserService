package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.ofs.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

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

}
