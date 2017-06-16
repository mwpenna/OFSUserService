package com.ofs.validators.user;

import com.ofs.models.User;
import com.ofs.server.errors.BadRequestException;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserSearchValidation;
import com.ofs.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSearchValidator implements Validator<User> {

    private final List<UserSearchValidation> VALIDATIONS = new ArrayList<>();

    @Autowired
    public UserSearchValidator(List<UserSearchValidation> validations) {
        validations.forEach(validation ->
                VALIDATIONS.add(validation)
        );
    }

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        for (UserSearchValidation validation : VALIDATIONS) {
            validation.validate(user, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }

    @Override
    public void validate(ChangeSet changeSet, User user, OFSErrors errors) throws Exception {
        for (UserSearchValidation validation : VALIDATIONS) {
            validation.validate(changeSet, user, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
