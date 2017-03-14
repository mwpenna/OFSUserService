package com.ofs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ofs.server.model.BaseOFSEntity;
import com.ofs.utils.StringUtils;
import lombok.Data;

import java.net.URI;
import java.util.UUID;

@Data
public class Company extends BaseOFSEntity{

    private URI href;
    private String name;
    private UUID id;

    @JsonIgnore
    public String getIdFromHref() {
        return StringUtils.getIdFromURI(getHref());
    }

}
