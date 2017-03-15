package com.ofs.repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.ofs.utils.GlobalConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CouchbaseFactory {

    private CouchbaseEnvironment env;

    private final String COMPANY_BUCKET = "company";
    private final String USER_BUCKET = "users";

    @Autowired
    GlobalConfigs globalConfigs;

    CouchbaseFactory() throws Exception {
        this.env = createInstance();
    }

    protected  @Bean Cluster cluster() {
        return CouchbaseCluster.create(this.getEnv(), globalConfigs.getClusterHostName());
    }

    protected @Bean Bucket companyBucket() {
        return cluster().openBucket(COMPANY_BUCKET, globalConfigs.getClusterPassword());
    }


    protected  @Bean Bucket userBucket() {
        return cluster().openBucket(USER_BUCKET, globalConfigs.getClusterPassword());
    }

    protected CouchbaseEnvironment createInstance() throws Exception {
        return DefaultCouchbaseEnvironment.builder()
                .connectTimeout(TimeUnit.SECONDS.toMillis(10))
                .ioPoolSize(20)
                .build();
    }

    public CouchbaseEnvironment getEnv() {
        return env;
    }

    public Bucket getCompanyBucket() {
        return companyBucket();
    }

    public Bucket getUserBucket() {
        return userBucket();
    }
}
