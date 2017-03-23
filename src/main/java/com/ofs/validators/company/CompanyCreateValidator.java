package com.ofs.validators.company;

import com.ofs.models.Company;
import com.ofs.server.errors.BadRequestException;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.CompanyCreateValidation;
import com.ofs.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyCreateValidator implements Validator<Company> {

    private final List<CompanyCreateValidation> VALIDATIONS = new ArrayList<>();

    @Autowired
    public CompanyCreateValidator(List<CompanyCreateValidation> validations) {
        validations.forEach(validation ->
                VALIDATIONS.add(validation)
        );
    }

    @Override
    public void validate(Company company, OFSErrors errors) throws Exception {
        for (CompanyCreateValidation validation : VALIDATIONS) {
            validation.validate(company, errors);
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
