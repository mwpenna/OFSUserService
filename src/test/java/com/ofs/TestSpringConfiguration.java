package com.ofs;

import com.ofs.integrationHelpers.MockAuthClient;
import com.ofs.repository.CompanyRepository;
import com.ofs.repository.ConnectionManager;
import com.ofs.repository.UserRepository;
import com.ofs.server.client.AuthenticationClient;
import com.ofs.server.security.AuthInterceptor;
import com.ofs.service.UserService;
import com.ofs.validators.user.UserCreateValidator;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class TestSpringConfiguration {

    @Bean
    @Primary
    public ConnectionManager connectionManager() {
        return mock(ConnectionManager.class);
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

    @Bean
    @Primary
    public UserService userService() { return mock(UserService.class); }
}
