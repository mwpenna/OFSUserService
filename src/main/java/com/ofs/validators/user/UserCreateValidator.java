package com.ofs.validators.user;

import com.ofs.models.User;
import com.ofs.server.errors.BadRequestException;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserCreateValidation;
import com.ofs.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserCreateValidator implements Validator<User> {

    private final List<UserCreateValidation> VALIDATIONS = new ArrayList<>();

    @Autowired
    public UserCreateValidator(List<UserCreateValidation> validations) {
        validations.forEach(validation ->
                VALIDATIONS.add(validation)
        );
    }

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        for (UserCreateValidation validation : VALIDATIONS) {
            validation.validate(user, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
