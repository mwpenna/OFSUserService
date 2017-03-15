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

    private URI href;
    private String name;
    private UUID id;

    public Company() {

    }

    public Company(URI href) {
        super(href);
    }

    public Company(Map map) {
        this.setId(UUID.fromString((String)map.get("id")));
        this.setName((String)map.get("name"));
        this.setHref(URI.create((String)map.get("href")));
    }

    @JsonIgnore
    public String getIdFromHref() {
        return StringUtils.getIdFromURI(getHref());
    }

    @JsonIgnore
    public static Company getCompany(Map companyMap) {
        Company company = new Company();

        company.setId(UUID.fromString((String)companyMap.get("id")));
        company.setName((String)companyMap.get("name"));
        company.setHref(URI.create((String)companyMap.get("href")));

        return company;
    }

}
