package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.server.errors.UnauthorizedException;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.server.security.SecurityContext;
import com.ofs.server.security.Subject;
import com.ofs.validations.UserDeleteValidation;
import org.springframework.stereotype.Component;

@Component
public class ValidateDeletePermissions implements UserDeleteValidation{

    @Override
    public void validate(String id, OFSErrors errors) throws Exception {
        Subject subject = SecurityContext.getSubject();

        if(!subject.getRole().equals(User.Role.SYSTEM_ADMIN.toString())) {
            throw new UnauthorizedException("OAuth", "OFSServer");
        }
    }

    @Override
    public void validate(ChangeSet changeSet, String id, OFSErrors errors) throws Exception {
        validate(id, errors);
    }
}
