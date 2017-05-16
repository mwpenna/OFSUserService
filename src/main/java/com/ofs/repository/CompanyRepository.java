package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;

import com.ofs.models.Company;

import com.ofs.server.repository.BaseCouchbaseRepository;
import com.ofs.server.repository.ConnectionManager;
import com.ofs.server.repository.OFSRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Component
@OFSRepository(value = "company")
public class CompanyRepository extends BaseCouchbaseRepository<Company> {

    @Autowired
    ConnectionManager connectionManager;

    public void addCompany(Company company) throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        add(company.getId().toString(), connectionManager.getBucket("company"), company);
    }

    public Optional<Company> getCompanyById(String id) throws Exception {
        if(id == null) {
            return Optional.empty();
        }

        try {
            ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(generateGetByIdQuery(), generateGetByIdParameters(id));
            return queryForObjectByParameters(query, connectionManager.getBucket("company"), Company.class);
        }
        catch (NoSuchElementException e) {
            log.warn("Company id {} does not exists", id, e);
            return Optional.empty();
        }
    }

    public Optional<Company> getCompanyByName(String name) throws Exception {
        if(name == null) {
            return Optional.empty();
        }

        try{
            ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(
                    generateGetByNameQuery(), generateGetByNameParameters(name));
            return queryForObjectByParameters(query, connectionManager.getBucket("company"), Company.class);
        }
        catch (NoSuchElementException e) {
            log.info("No results returned for getCompanyByName with company name: {}", name);
            return Optional.empty();
        }
    }

    private String generateGetByIdQuery() {
        return "SELECT `" + connectionManager.getBucket("company").name() + "`.* FROM `" + connectionManager.getBucket("company").name() + "` where id = $id";
    }

    private JsonObject generateGetByIdParameters(String id) {
        return JsonObject.create().put("$id", id);
    }

    private String generateGetByNameQuery() {
        return "SELECT `" + connectionManager.getBucket("company").name() + "`.* FROM `" + connectionManager.getBucket("company").name() + "` where name = $name";
    }

    private JsonObject generateGetByNameParameters(String name) {
        return JsonObject.create().put("$name", name);
    }
}
