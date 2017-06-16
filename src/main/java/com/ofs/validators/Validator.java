package com.ofs.validators;


import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;

public interface Validator<T> {
    void validate(T t, OFSErrors errors) throws Exception;
    void validate(ChangeSet changeSet, T t, OFSErrors errors) throws Exception;
}
