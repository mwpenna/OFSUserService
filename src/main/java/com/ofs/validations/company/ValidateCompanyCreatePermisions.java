package com.ofs.validations.company;

import com.ofs.models.Company;
import com.ofs.models.User;
import com.ofs.server.errors.UnauthorizedException;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.server.security.SecurityContext;
import com.ofs.server.security.Subject;
import com.ofs.validations.CompanyCreateValidation;
import org.springframework.stereotype.Component;

@Component
public class ValidateCompanyCreatePermisions implements CompanyCreateValidation {
    @Override
    public void validate(Company company, OFSErrors errors) throws Exception {
        Subject subject = SecurityContext.getSubject();

        if(!subject.getRole().equals(User.Role.SYSTEM_ADMIN.toString())) {
            throw new UnauthorizedException("OAuth", "OFSServer");
        }
    }

    @Override
    public void validate(ChangeSet changeSet, Company company, OFSErrors errors) throws Exception {
        validate(company, errors);
    }
}
