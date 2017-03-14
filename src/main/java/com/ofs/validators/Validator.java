package com.ofs.validators;


import com.ofs.server.model.OFSErrors;

public interface Validator<T> {
    void validate(T t, OFSErrors errors);
}
