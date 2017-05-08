package com.ofs.validations.company;

import com.ofs.models.Company;
import com.ofs.models.User;
import com.ofs.server.errors.UnauthorizedException;
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
}
