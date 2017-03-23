package com.ofs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ofs.server.model.BaseOFSEntity;
import com.ofs.utils.StringUtils;
import lombok.Data;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Data
public class Company extends BaseOFSEntity{

    private String name;
    private UUID id;

    public Company() {

    }

    public Company(URI href) {
        super(href);
    }

    public Company(Map map) {
        String id = (String)map.get("id");
        this.setId(id != null ? UUID.fromString(id) : null);

        this.setName((String)map.get("name"));

        String href = (String)map.get("href");
        this.setHref(href != null ? URI.create(href) : null);
    }

    @JsonIgnore
    public String getIdFromHref() {
        return StringUtils.getIdFromURI(getHref());
    }

    @JsonIgnore
    public static Company getCompany(Map companyMap) {
        Company company = new Company();

        company.setHref(URI.create((String)companyMap.get("href")));
        company.setName((String)companyMap.get("name"));

        String id = (String)companyMap.get("id");
        company.setId(id != null ? UUID.fromString(id) : UUID.fromString(StringUtils.getIdFromURI(company.getHref())));

        return company;
    }

}
