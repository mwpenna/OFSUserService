package com.ofs.validations.user;

import com.ofs.models.Company;
import com.ofs.models.User;
import com.ofs.repository.CompanyRepository;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserCreateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateCompany implements UserCreateValidation {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Optional<Company> optionalcompany = companyRepository.getCompanyById(user.getCompany().getIdFromHref());

        if(!optionalcompany.isPresent()) {
            errors.rejectValue("user.company.id.invalid", "company.id", "Invalid company id. Company does not exits.");
            return;
        }
        else {
            Company company = optionalcompany.get();
            //TODO: Validate Authenticated User token company is same company as new user is being created for
        }
    }
}
