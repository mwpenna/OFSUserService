package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.client.java.error.TemporaryFailureException;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.ofs.models.User;
import com.ofs.server.errors.NotFoundException;
import com.ofs.server.errors.ServiceUnavailableException;
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
        Objects.requireNonNull(user);

        log.info("Attempting to add user with id: {}", user.getId());
        add(user.getId().toString(), couchbaseFactory.getUserBucket(), user);
        log.info("User with id: {} has been added", user.getId());
    }

    public Optional<User> getUserById(String id) {
        log.info("Attempting to retreive user with id: {}", id);
        if(id == null) {
            log.warn("Id cannot be null");
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
        catch (TemporaryFailureException e) {
            log.error("Temporary Failure with couchbase occured" , e);
            throw new ServiceUnavailableException();
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
        catch (TemporaryFailureException e) {
            log.error("Temporary Failure with couchbase occured" , e);
            throw new ServiceUnavailableException();
        }
    }

    public void updateUser(User user) throws com.fasterxml.jackson.core.JsonProcessingException {
        Objects.requireNonNull(user);

        try {
            log.info("Attempting to update user with id: {}", user.getId());
            update(user.getId().toString(), couchbaseFactory.getUserBucket(), user);
            log.info("user with id: {} has been updated", user.getId());
        }
        catch(DocumentDoesNotExistException e) {
            log.warn("User with id: {} was not found", user.getId());
            throw new NotFoundException();
        }
        catch (TemporaryFailureException e) {
            log.error("Temporary Failure with couchbase occured" , e);
            throw new ServiceUnavailableException();
        }
    }

    public void deleteUserById(String id) {
        Objects.requireNonNull(id);

        try{
            log.info("Attempting to delete user with id: {}", id);
            delete(id, couchbaseFactory.getUserBucket());
            log.info("user with id: {} has been delete", id);
        }
        catch (DocumentDoesNotExistException e) {
            log.warn("User with id: {} was not found", id);
            throw new NotFoundException();
        }
        catch (TemporaryFailureException e) {
            log.error("Temporary Failure with couchbase occured" , e);
            throw new ServiceUnavailableException();
        }
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
