package com.ofs.validations;

import com.ofs.models.Company;
import com.ofs.models.User;
import com.ofs.repository.CompanyRepository;
import com.ofs.server.model.OFSErrors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ValidateCompanyTest {

    @InjectMocks
    ValidateCompany objectUnderTest;

    @Mock
    CompanyRepository companyRepository;

    private User user;
    private UUID id;

    @Before
    public void setup() {
        initMocks(this);

        id = UUID.randomUUID();
        user = new User();

        Company company = new Company();
        company.setId(id);
        company.setName("Name");
        company.setHref(URI.create("localhost/company/id/"+id));

        user.setCompany(company);
    }

    @Test
    public void valididateCompanyNotFound_shouldAddCompanyInvalidErrorToErrorList() throws Exception {
        when(companyRepository.getCompanyById(any())).thenReturn(Optional.empty());
        OFSErrors errors = new OFSErrors();
        objectUnderTest.validate(user, errors);
        assertTrue(errors.size()>0);

        errors.forEach(ofsError -> {
            assertEquals(ofsError.getCode(), "user.company.id.invalid");
            assertEquals(ofsError.getProperties().get("field"),"company.id");
            assertEquals(ofsError.getDeveloperMessage(), "Invalid company id. Company does not exits.");
        });
    }

}