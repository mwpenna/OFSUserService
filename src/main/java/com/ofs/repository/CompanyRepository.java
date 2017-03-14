package com.ofs.repository;

import com.couchbase.client.core.utils.Observables;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.ofs.models.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import rx.Observable;
import rx.functions.Func1;

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

        return Optional.of(queryGetById(id));
    }

    private String generateGetByIdQuery(String id) {
        return "SELECT `" + bucket().name() + "`.* FROM `" + bucket().name() + "` id = \"$id\"";
    }

    private JsonObject generateGetByIdParameters(String id) {
        return JsonObject.create().put("id", id);
    }

    private Company queryGetById(String id) {
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(generateGetByIdQuery(id), generateGetByIdParameters(id));
        return (Company) bucket().async().query(query)
                .filter(new Func1<AsyncN1qlQueryResult, Boolean>() {
                    @Override
                    public Boolean call(AsyncN1qlQueryResult asyncN1qlQueryResult) {
                        AsyncN1qlQueryRow row = asyncN1qlQueryResult.rows();
                    }
                })
                .flatMap(AsyncN1qlQueryResult::rows)
                .filter(result -> result.value().isEmpty())
                .map(result -> result.value().toMap())
                .timeout(10, TimeUnit.SECONDS)
                .toBlocking()
                .single();
    }
}
