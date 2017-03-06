package com.ofs.models;

import com.ofs.server.model.BaseOFSEntity;
import lombok.Data;

import java.net.URI;
import java.util.UUID;

@Data
public class Company extends BaseOFSEntity{

    private URI href;
    private String name;
    private UUID id;

}
