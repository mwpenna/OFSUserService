package com.ofs.repository;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;

import com.ofs.models.Company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class CompanyRepository extends BaseCouchbaseRepository<Company> {

    @Autowired
    CouchbaseFactory couchbaseFactory;

    public Optional<Company> getCompanyById(String id) throws Exception {
        if(id == null) {
            return Optional.empty();
        }

        try {
//            return Optional.of(queryGetById(id));
            ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(generateGetByIdQuery(), generateGetByIdParameters(id));
            return Optional.of(queryByParameters(query, Company.class));

        }
        catch (NoSuchElementException e) {
            log.warn("Company id {} does not exists", id, e);
            return Optional.empty();
        }
    }

    private String generateGetByIdQuery() {
        return "SELECT `" + couchbaseFactory.getCompanyBucket().name() + "`.* FROM `" + couchbaseFactory.getCompanyBucket().name() + "` where id = $id";
    }

    private JsonObject generateGetByIdParameters(String id) {
        return JsonObject.create().put("$id", id);
    }

//    private Company queryGetById(String id) {
//        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(generateGetByIdQuery(), generateGetByIdParameters(id));
//        Map companyMap =  couchbaseFactory.getCompanyBucket().async().query(query)
//                .flatMap(AsyncN1qlQueryResult::rows)
//                .map(result -> result.value().toMap())
//                .timeout(10, TimeUnit.SECONDS)
//                .toBlocking()
//                .single();
//
//        return Company.getCompany(companyMap);
//    }
}
