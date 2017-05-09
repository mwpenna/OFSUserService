package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.server.errors.UnauthorizedException;
import com.ofs.server.model.OFSErrors;
import com.ofs.server.security.SecurityContext;
import com.ofs.server.security.Subject;
import com.ofs.utils.StringUtils;
import com.ofs.validations.UserGetValidation;
import com.ofs.validations.UserUpdateValidation;
import org.springframework.stereotype.Component;

@Component
public class ValidateCustomerRole implements UserGetValidation, UserUpdateValidation {

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Subject subject = SecurityContext.getSubject();

        if(subject.getRole().equals(User.Role.CUSTOMER.toString())) {
            if(!StringUtils.getIdFromURI(subject.getHref()).equals(user.getId().toString())) {
                throw new UnauthorizedException("OAuth", "OFSServer");
            }
        }
    }
}
