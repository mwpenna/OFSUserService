package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.ofs.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepository extends BaseCouchbaseRepository<User> {

    @Autowired
    CouchbaseFactory couchbaseFactory;

    public void addUser(User user) throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        JsonObject jsonObject = JsonObject.fromJson(ofsObjectMapper.writeValueAsString(user));
        JsonDocument jsonDocument = JsonDocument.create(user.getId().toString(), jsonObject);
        couchbaseFactory.getUserBucket().insert(jsonDocument);
    }

    public Optional<User> getUserById(String id) {
        if(id == null) {
            return Optional.empty();
        }

        return queryForObjectById(id, couchbaseFactory.getUserBucket(), User.class);
    }

    public Optional<User> getUserByUserName(String username) throws Exception{
        if(username == null) {
            return Optional.empty();
        }

        try {
            ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(
                generateGetByUserNameQuery(), generateGetByUserNameParameters(username));
            return queryForObjectByParameters(query, couchbaseFactory.getUserBucket(), User.class);
        }
        catch (NoSuchElementException e) {
            log.info("No results returned for getUserByUserName with username: {}", username);
            return Optional.empty();
        }
    }

    public Optional<User> getUserByEmailAddress(String emailAddress) throws Exception {
        if(emailAddress == null) {
            return Optional.empty();
        }

        try {
            ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(
                    generateGetByEmailAddressQuery(), generateGetByEmailAddressParameters(emailAddress));
            return queryForObjectByParameters(query, couchbaseFactory.getUserBucket(), User.class);
        }
        catch (NoSuchElementException e) {
            log.info("No results returned for getUserByEmailAddress with emailaddress: {}", emailAddress);
            return Optional.empty();
        }
    }

    public void updateUserToken(User user) throws com.fasterxml.jackson.core.JsonProcessingException {
        Objects.requireNonNull(user);

        JsonDocument userDocument = couchbaseFactory.getUserBucket().getAndLock(user.getId().toString(), 5);
        JsonDocument updatedDocument = modifyJsonDocument(userDocument, user);
        couchbaseFactory.getUserBucket().replace(updatedDocument);
    }

    private JsonDocument modifyJsonDocument(JsonDocument jsonDocument, User user) throws com.fasterxml.jackson.core.JsonProcessingException {
        JsonObject jsonObject = JsonObject.fromJson(ofsObjectMapper.writeValueAsString(user));
        return JsonDocument.create(user.getId().toString(), jsonObject, jsonDocument.cas());
    }

    private String generateGetByUserNameQuery() {
        return "SELECT `" + couchbaseFactory.getUserBucket().name() + "`.* FROM `" + couchbaseFactory.getUserBucket().name() + "` where userName = $userName";
    }

    private JsonObject generateGetByUserNameParameters(String userName) {
        return JsonObject.create().put("$userName", userName);
    }

    private String generateGetByEmailAddressQuery() {
        return "SELECT `" + couchbaseFactory.getUserBucket().name() + "`.* FROM `" + couchbaseFactory.getUserBucket().name() + "` where emailAddress = $emailAddress";
    }

    private JsonObject generateGetByEmailAddressParameters(String emailAddress) {
        return JsonObject.create().put("$emailAddress", emailAddress);
    }
}
