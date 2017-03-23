package com.ofs.controller;

import com.ofs.models.Company;
import com.ofs.repository.CompanyRepository;
import com.ofs.server.OFSController;
import com.ofs.server.OFSServerId;
import com.ofs.server.form.OFSServerForm;
import com.ofs.server.form.ValidationSchema;
import com.ofs.server.model.OFSErrors;
import com.ofs.validators.company.CompanyCreateValidator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.UUID;

@Slf4j
@OFSController
@RequestMapping(value="/company", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyCreateValidator companyCreateValidator;

    @ValidationSchema(value = "/company-create.json")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@OFSServerId URI id, OFSServerForm<Company> form) throws Exception {
        Company company = form.create(id);
        defaultValues(company);

        OFSErrors errors = new OFSErrors();
        companyCreateValidator.validate(company, errors);

        companyRepository.addCompany(company);
        return ResponseEntity.created(id).build();
    }

    private void defaultValues(Company company) {
        company.setName(company.getName().toLowerCase());
        company.setId(UUID.fromString(company.getIdFromHref()));
    }

}
