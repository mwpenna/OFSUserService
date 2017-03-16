package com.ofs.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.ofs.models.User;
import com.ofs.server.model.BaseOFSEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

public abstract class BaseCouchbaseRepository<T> {

    @Autowired
    CouchbaseFactory couchbaseFactory;

    ObjectMapper objectMapper = new ObjectMapper();

    public Optional<T> queryForObjectByParameters(ParameterizedN1qlQuery query, Class<T> clazz) throws Exception {
        T entity;

        N1qlQueryResult queryResult = couchbaseFactory.getUserBucket().query(query);

        Map resultMap = null;

        for(N1qlQueryRow row : queryResult) {
            resultMap =  row.value().toMap();
        }

        try {
            if(resultMap == null || resultMap.isEmpty()) {
                return Optional.empty();
            }

            Constructor<T> constructor = clazz.getConstructor(Map.class);
            entity = constructor.newInstance(resultMap);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(format("%s is missing a map constructor", clazz.getTypeName()), e);
        }
        catch(IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(format("Failed to create entity %s", clazz.getTypeName()), e);
        }

        return Optional.of(entity);
    }
}
