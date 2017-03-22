package com.ofs;

import com.ofs.repository.CompanyRepository;
import com.ofs.repository.CouchbaseFactory;
import com.ofs.repository.UserRepository;
import com.ofs.validators.UserCreateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class TestSpringConfiguration {

    @Bean
    @Primary
    public CouchbaseFactory couchbaseFactory() {
        return mock(CouchbaseFactory.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    @Primary
    public CompanyRepository companyRepository() {
        return mock(CompanyRepository.class);
    }

    @Bean
    @Primary
    public UserCreateValidator userCreateValidator() {
        return mock(UserCreateValidator.class);
    }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}