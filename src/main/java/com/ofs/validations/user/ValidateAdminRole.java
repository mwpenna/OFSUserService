package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.server.errors.UnauthorizedException;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.server.security.SecurityContext;
import com.ofs.server.security.Subject;
import com.ofs.utils.StringUtils;
import com.ofs.validations.UserGetByCompanyId;
import com.ofs.validations.UserGetValidation;
import com.ofs.validations.UserUpdateValidation;
import org.springframework.stereotype.Component;

@Component
public class ValidateAdminRole implements UserGetValidation, UserUpdateValidation {
    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Subject subject = SecurityContext.getSubject();

        if(subject.getRole().equals(User.Role.ADMIN.toString())) {
            if(!StringUtils.getIdFromURI(subject.getCompanyHref()).equals(user.getCompany().getIdFromHref())) {
                throw new UnauthorizedException("OAuth", "OFSServer");
            }
        }
    }

    @Override
    public void validate(ChangeSet changeSet, User user, OFSErrors errors) throws Exception {
        validate(user, errors);
    }
}
