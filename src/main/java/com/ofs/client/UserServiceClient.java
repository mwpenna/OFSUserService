package com.ofs.client;

import com.ofs.models.JWTSubject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("http://USERSERVICE")
public interface UserServiceClient {
    @RequestMapping(value = "/users/authenticate", method= GET)
    JWTSubject authenticate(@RequestHeader("Authorization") String token);
}
