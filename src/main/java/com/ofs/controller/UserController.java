package com.ofs.controller;

import com.ofs.models.BasicAuthUser;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.OFSController;
import com.ofs.server.OFSServerId;
import com.ofs.server.errors.ForbiddenException;
import com.ofs.server.errors.NotFoundException;
import com.ofs.server.form.OFSServerForm;
import com.ofs.server.form.ValidationSchema;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.service.UserService;
import com.ofs.utils.GlobalConfigs;
import com.ofs.validators.user.UserGetTokenValidator;
import com.ofs.validators.user.UserCreateValidator;

import lombok.extern.slf4j.Slf4j;

import org.jasypt.util.password.StrongPasswordEncryptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@OFSController
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCreateValidator userCreateValidator;

    @Autowired
    private UserGetTokenValidator getTokenValidator;

    @Autowired
    GlobalConfigs globalConfigs;

    @Autowired
    @Qualifier("ofsObjectMapper")
    protected com.fasterxml.jackson.databind.ObjectMapper ofsObjectMapper;

    @Autowired
    UserService userService;

    @ValidationSchema(value = "/user-create.json")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@OFSServerId URI id, OFSServerForm<User> form) throws Exception{
        User user = form.create(id);
        defaultUserValues(user);
        user.setPassword(encryptPassword(user.getPassword()));

        OFSErrors errors = new OFSErrors();
        userCreateValidator.validate(user, errors);

        userRepository.addUser(user);
        return ResponseEntity.created(id).build();
    }

    @GetMapping(value= "/id/{id}")
    public User getUserById(@PathVariable("id") String id) {
        log.debug("Fetching User with id {}", id);
        Optional<User> optionalUser = userRepository.getUserById(id);

        if(optionalUser.isPresent()) {
            log.debug("User found with id {}, id");
            return optionalUser.get();
        }
        else {
            log.error("User not found");
            throw new NotFoundException();
        }
    }

    @ValidationSchema(value = "/user-update.json")
    @PostMapping(value = "/id/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable String id, OFSServerForm<User> form) throws IOException {
        Optional<User> userOptional = userRepository.getUserById(id);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            ChangeSet changeSet = form.update(user);

            if(changeSet.size()>0) {
                if(changeSet.contains("password")) {
                    user.setPassword(encryptPassword(user.getPassword()));
                }

                userRepository.updateUser(user);
            }
            return ResponseEntity.noContent().build();
        }
        else {
            log.error("User with id: {} not found", id);
            throw new NotFoundException();
        }
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        userRepository.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/getToken")
    public ResponseEntity getToken(@RequestHeader HttpHeaders headers) throws Exception {

        String authString = getValidAuthHeader(headers);
        BasicAuthUser basicAuthUser = getUserFromAuthentication(authString);

        OFSErrors errors = new OFSErrors();
        getTokenValidator.validate(getUserFromAuthentication(authString), errors);

        return ResponseEntity.ok(userService.getToken(basicAuthUser));
    }

    @GetMapping(value = "/authenticate")
    public ResponseEntity authenticateUser() { return ResponseEntity.noContent().build();}

    private String encryptPassword(String password) {
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.encryptPassword(password);
    }

    private void defaultUserValues(User user) {
        user.setId(UUID.fromString(user.getIdFromHref()));
        user.setActiveFlag(true);
    }


    private String getValidAuthHeader(HttpHeaders headers) {

        List<String> authHeader = headers.get("authorization");
        if(!validateAuthorizationHeader(authHeader)) {
            throw new ForbiddenException();
        }

        String authString = headers.get("authorization").get(0);
        if(!validateAuthString(authString)) {
            throw new ForbiddenException();
        }

        return authString;
    }

    private boolean validateAuthString(String authString) {
        if(authString == null || authString.split(" ").length < 2) {
            return false;
        }

        return true;
    }

    private boolean validateAuthorizationHeader(List<String> authHeader) {
        if(authHeader == null || authHeader.isEmpty()) {
            return false;
        }

        return true;
    }

    private BasicAuthUser getUserFromAuthentication(String authString) {
        String authentication = authString.split(" ")[1];
        String decodedAuthString = new String(Base64.getDecoder().decode(authentication.getBytes()));
        String [] authArray = decodedAuthString.split(":");
        return BasicAuthUser.basicAuthUserFactory(authArray[0], authArray[1]);
    }
}
