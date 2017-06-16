package com.ofs.validations.company;

import com.ofs.models.Company;
import com.ofs.repository.CompanyRepository;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.CompanyCreateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateCompanyName implements CompanyCreateValidation {

    @Autowired
    CompanyRepository companyRepository;


    @Override
    public void validate(Company company, OFSErrors errors) throws Exception {
        Optional optionalCompany = companyRepository.getCompanyByName(company.getName());

        if(optionalCompany.isPresent()) {
            errors.rejectValue("company.name.exists", "company.name", "Invalid company name. Company name already exists.");
        }
    }

    @Override
    public void validate(ChangeSet changeSet, Company company, OFSErrors errors) throws Exception {
        validate(company, errors);
    }
}
