package com.ofs.repository;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

import java.util.concurrent.TimeUnit;

@Component
public class ConnectionManager {

    @Autowired
    ApplicationContext applicationContext;

    private static final String COMPANY_BUCKET = "company";
    private static final String USER_BUCKET = "users";

    private static final ConnectionManager connectionManager = new ConnectionManager();

    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    static CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
            .connectTimeout(TimeUnit.SECONDS.toMillis(10))
            .ioPoolSize(20)
            .build();

    static Cluster cluster = CouchbaseCluster.create(env, "10.226.190.77:8091");
    static Bucket companyBucket = cluster.openBucket(COMPANY_BUCKET, "");
    static Bucket userBucket = cluster.openBucket(USER_BUCKET, "");

    public static void disconnect() {
        cluster.disconnect();
    }

    public static Bucket getCompanyBucket() {
        return companyBucket;
    }

    public static Bucket getUserBucket() {
        return userBucket;
    }

}
