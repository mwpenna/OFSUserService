package com.ofs.repository;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.ofs.utils.GlobalConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

import java.util.concurrent.TimeUnit;

@Component
public class ConnectionManager {

    @Autowired
    GlobalConfigs globalConfigs;

    private final String COMPANY_BUCKET = "company";
    private final String USER_BUCKET = "users";

    private CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
            .connectTimeout(TimeUnit.SECONDS.toMillis(10))
            .ioPoolSize(20)
            .build();

    private Cluster cluster;
    private Bucket companyBucket;
    private Bucket userBucket;

    public void disconnect() {
        cluster.disconnect();
    }

    private Cluster getCluster() {
        if(cluster == null) {
            cluster = CouchbaseCluster.create(env, globalConfigs.getClusterHostName());
        }
        return cluster;
    }

    public Bucket getCompanyBucket() {
        if(companyBucket == null) {
            companyBucket = getCluster().openBucket(COMPANY_BUCKET, globalConfigs.getClusterPassword());
        }
        return companyBucket;
    }

    public Bucket getUserBucket() {
        if(userBucket == null) {
            userBucket = getCluster().openBucket(USER_BUCKET, globalConfigs.getClusterPassword());
        }
        return userBucket;
    }
}
