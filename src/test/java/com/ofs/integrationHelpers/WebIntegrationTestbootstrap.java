package com.ofs.integrationHelpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofs.repository.CompanyRepository;
import com.ofs.repository.UserRepository;
import com.ofs.service.UserService;
import com.ofs.validators.user.UserCreateValidator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.mockito.Mockito.reset;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class WebIntegrationTestbootstrap {

    @Value("${local.server.port}")
    public int port;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CompanyRepository companyRepository;

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public UserCreateValidator userCreateValidator;

    @Autowired
    @Qualifier("ofsObjectMapper")
    public ObjectMapper ofsObjectMapper;

    @Autowired
    public UserService userService;

    @Before
    public void superSetup() {
        reset(userRepository);
        reset(companyRepository);

        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response)
                    throws IOException
            {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response)
                    throws IOException
            {

            }
        });
    }

    protected HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        return headers;
    }

    protected HttpHeaders createJSONHeaders() {
        HttpHeaders headers = this.createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public String apiUrl(String path) {
        return "http://localhost:" + port + "/" + path;
    }
}
