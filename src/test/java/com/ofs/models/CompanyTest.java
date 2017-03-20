package com.ofs.models;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class CompanyTest {

//    private Company company;
    private Map companyMap;
    private UUID id;
    private String href;
    private String name;

    @Before
    public void setup() {

        id = UUID.randomUUID();
        href = "localhost/company/id/"+id;
        name = "test";

//        company = new Company();
//        company.setId(id);
//        company.setName(name);
//        company.setHref(URI.create(href));

        companyMap = new HashMap();
        companyMap.put("href", href);
        companyMap.put("name", name);
        companyMap.put("id", id.toString());
    }

    @Test
    public void getCompanyShouldCreateNewCompany() {
       Company company =  Company.getCompany(companyMap);
       assertEquals(company.getHref(), URI.create(href));
       assertEquals(company.getId(), id);
       assertEquals(company.getName(), name);
       assertEquals(company.getIdFromHref(), id.toString());
    }

}