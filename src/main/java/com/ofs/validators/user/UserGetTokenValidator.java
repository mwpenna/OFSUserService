package com.ofs.validators.user;

import com.ofs.models.BasicAuthUser;
import com.ofs.server.errors.BadRequestException;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserGetTokenValidation;
import com.ofs.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserGetTokenValidator implements Validator<BasicAuthUser> {

    private final List<UserGetTokenValidation> VALIDATIONS = new ArrayList<>();

    @Autowired
    public UserGetTokenValidator(List<UserGetTokenValidation> validations) {
        validations.forEach(validation -> VALIDATIONS.add(validation));
    }


    @Override
    public void validate(BasicAuthUser basicAuthUser, OFSErrors errors) throws Exception {
        for(UserGetTokenValidation validation : VALIDATIONS) {
            validation.validate(basicAuthUser, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
