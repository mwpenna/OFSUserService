package com.ofs.validators.user;

import com.ofs.server.errors.BadRequestException;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserDeleteValidation;
import com.ofs.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDeleteValidator implements Validator<String> {

    private final List<UserDeleteValidation> VALIDATIONS = new ArrayList<>();

    @Autowired
    public UserDeleteValidator(List<UserDeleteValidation> validations) {
        validations.forEach(validation ->
                VALIDATIONS.add(validation)
        );
    }

    @Override
    public void validate(String id, OFSErrors errors) throws Exception {
        for (UserDeleteValidation validation : VALIDATIONS) {
            validation.validate(id, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
