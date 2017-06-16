package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.server.errors.UnauthorizedException;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.server.security.SecurityContext;
import com.ofs.server.security.Subject;
import com.ofs.validations.UserGetByCompanyId;
import com.ofs.validations.UserSearchValidation;
import org.springframework.stereotype.Component;

@Component
public class ValidateGetUsers implements UserGetByCompanyId, UserSearchValidation{
    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Subject subject = SecurityContext.getSubject();

        if((subject.getRole().toString().equalsIgnoreCase(User.Role.CUSTOMER.toString())) || (subject.getRole().toString().equalsIgnoreCase(User.Role.ACCOUNT_MANAGER.toString()))
                || (subject.getRole().toString().equalsIgnoreCase(User.Role.WAREHOUSE.toString()))) {
            throw new UnauthorizedException("OAuth", "OFSServer");
        }
    }

    @Override
    public void validate(ChangeSet changeSet, User user, OFSErrors errors) throws Exception {
        validate(user, errors);
    }
}
