package com.ofs.validators.user;

import com.ofs.models.User;
import com.ofs.server.errors.BadRequestException;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserUpdateValidation;
import com.ofs.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUpdateValidator implements Validator<User> {

    private final List<UserUpdateValidation> VALIDATIONS = new ArrayList<>();

    @Autowired
    public UserUpdateValidator(List<UserUpdateValidation> validations) {
        validations.forEach(validation ->
                VALIDATIONS.add(validation)
        );
    }

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        for (UserUpdateValidation validation : VALIDATIONS) {
            validation.validate(user, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
