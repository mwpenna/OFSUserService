package com.ofs.repository;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;

import com.ofs.server.errors.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public abstract class BaseCouchbaseRepository<T> {

    @Autowired
    CouchbaseFactory couchbaseFactory;

    @Autowired
    @Qualifier("ofsObjectMapper")
    protected com.fasterxml.jackson.databind.ObjectMapper ofsObjectMapper;

    public Optional<T> queryForObjectByParameters(ParameterizedN1qlQuery query, Bucket bucket, Class<T> clazz) throws Exception {

        Objects.requireNonNull(query);
        Objects.requireNonNull(clazz);

        N1qlQueryResult queryResult = queryForObject(query, bucket);

        Map resultMap = null;

        for(N1qlQueryRow row : queryResult) {
            resultMap =  row.value().toMap();
        }

        return mapResultsToObject(resultMap, clazz);
    }

    private N1qlQueryResult queryForObject(ParameterizedN1qlQuery query, Bucket bucket) {
        N1qlQueryResult queryResult;

        try{
            queryResult = bucket.query(query);
        }
        catch (BackpressureException | RequestCancelledException ex) {
            log.error("Exception occured with connection to couchbase : {}", ex);
            throw new ServiceUnavailableException();
        }
        catch (NoSuchElementException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            log.error("Exception occured with connection to couchbase : {}", ex);
            throw new ServiceUnavailableException();
        }

        return queryResult;
    }

    private Optional<T> mapResultsToObject(Map resultMap, Class clazz) {
        T entity;

        try {
            if(resultMap == null || resultMap.isEmpty()) {
                return Optional.empty();
            }

            Constructor<T> constructor = clazz.getConstructor(Map.class);
            entity = constructor.newInstance(resultMap);
            return Optional.of(entity);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(format("%s is missing a map constructor", clazz.getTypeName()), e);
        }
        catch(IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(format("Failed to create entity %s", clazz.getTypeName()), e);
        }
    }
}
