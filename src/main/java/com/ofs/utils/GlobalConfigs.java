package com.ofs.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class GlobalConfigs {

    @Value("${sharedSecret}")
    private String sharedSecret;
}
