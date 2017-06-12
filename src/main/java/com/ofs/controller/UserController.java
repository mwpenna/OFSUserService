package com.ofs.controller;

import com.ofs.models.BasicAuthUser;

import com.ofs.models.JWTSubject;
import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.OFSController;
import com.ofs.server.OFSServerId;
import com.ofs.server.errors.BadRequestException;
import com.ofs.server.errors.ForbiddenException;
import com.ofs.server.errors.NotFoundException;
import com.ofs.server.filter.views.Public;
import com.ofs.server.form.OFSServerForm;
import com.ofs.server.form.ValidationSchema;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.server.security.Authenticate;
import com.ofs.server.security.SecurityContext;
import com.ofs.server.security.Subject;
import com.ofs.service.UserService;
import com.ofs.utils.GlobalConfigs;
import com.ofs.utils.StringUtils;
import com.ofs.validators.user.UserDeleteValidator;
import com.ofs.validators.user.UserGetByCompanyIdValidator;
import com.ofs.validators.user.UserGetTokenValidator;
import com.ofs.validators.user.UserCreateValidator;

import com.ofs.validators.user.UserSearchValidator;
import com.ofs.validators.user.UserUpdateValidator;
import lombok.extern.slf4j.Slf4j;

import org.jasypt.util.password.StrongPasswordEncryptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@OFSController(resolver = "userResolver", filter = Public.class)
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCreateValidator userCreateValidator;

    @Autowired
    private UserGetTokenValidator getTokenValidator;

    @Autowired
    private UserUpdateValidator updateValidator;

    @Autowired
    private UserDeleteValidator userDeleteValidator;

    @Autowired
    private UserGetByCompanyIdValidator userGetByCompanyIdValidator;

    @Autowired
    private UserSearchValidator userSearchValidator;

    @Autowired
    GlobalConfigs globalConfigs;

    @Autowired
    @Qualifier("ofsObjectMapper")
    protected com.fasterxml.jackson.databind.ObjectMapper ofsObjectMapper;

    @Autowired
    UserService userService;

    @ValidationSchema(value = "/user-create.json")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Authenticate
    @CrossOrigin(origins = "*")
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
    @ResponseBody
    @Authenticate
    public User getUserById(@PathVariable("id") String id) throws Exception {
        log.debug("Fetching User with id {}", id);
        return userService.getUserById(id);
    }

    @GetMapping(value = "/token")
    @ResponseBody
    @Authenticate
    @CrossOrigin(origins = "*")
    public User getUserByToken() throws Exception {
        Subject subject = SecurityContext.getSubject();
        return userService.getUserById(StringUtils.getIdFromURI(subject.getHref()));
    }

    @ValidationSchema(value = "/user-update.json")
    @PostMapping(value = "/id/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Authenticate
    @CrossOrigin(origins = "*")
    public ResponseEntity update(@PathVariable String id, OFSServerForm<User> form) throws Exception {
        Optional<User> userOptional = userRepository.getUserById(id);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            OFSErrors errors = new OFSErrors();
            updateValidator.validate(user, errors);

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
    @Authenticate
    public ResponseEntity delete(@PathVariable("id") String id) throws Exception {
        OFSErrors errors = new OFSErrors();
        userDeleteValidator.validate(id, errors);

        userRepository.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @GetMapping(value="/getToken")
    @CrossOrigin(origins = "*")
    public ResponseEntity getToken(@RequestHeader HttpHeaders headers) throws Exception {

        String authString = getValidAuthHeader(headers);
        BasicAuthUser basicAuthUser = getUserFromAuthentication(authString);

        OFSErrors errors = new OFSErrors();
        getTokenValidator.validate(getUserFromAuthentication(authString), errors);

        return ResponseEntity.ok(userService.getToken(basicAuthUser));
    }

    @ResponseBody
    @GetMapping(value = "/authenticate")
    public JWTSubject authenticateUser(@RequestHeader HttpHeaders headers) throws IOException {
        String authString = getValidAuthHeader(headers);
        String token = getBeaerTokenFromAuthentication(authString);
        return userService.authenticate(token);
    }

    @ResponseBody
    @GetMapping(value = "/company/id/{id}")
    @Authenticate
    @CrossOrigin(origins = "*")
    public List<User> getUsersByCompanyId(@PathVariable("id") String id) throws Exception {
        log.debug("Fetching Users for company id {}", id);

        OFSErrors errors = new OFSErrors();
        userGetByCompanyIdValidator.validate(userService.getUserById(StringUtils.getIdFromURI(SecurityContext.getSubject().getHref())), errors);

        Optional<List<User>> optionalUser = userRepository.getUsersByCompanyId(id);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {
            return new ArrayList<>();
        }
    }

    @ResponseBody
    @PostMapping(value="search")
    @Authenticate
    @CrossOrigin(origins = "*")
    public List<User> search(OFSServerForm<User> form) throws Exception {
        Subject subject = SecurityContext.getSubject();
        log.debug("Fetching users for company id {}", StringUtils.getIdFromURI(subject.getCompanyHref()));

        OFSErrors errors = new OFSErrors();
        userSearchValidator.validate(userService.getUserById(StringUtils.getIdFromURI(SecurityContext.getSubject().getHref())), errors);

        Optional<List<User>> optionalUser = userRepository.getUsersByCompanyId(StringUtils.getIdFromURI(subject.getCompanyHref()));
        if(optionalUser.isPresent()) {
            return form.search(optionalUser.get());
        }
        else {
            return new ArrayList<>();
        }
    }

    private String encryptPassword(String password) {
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.encryptPassword(password);
    }

    private void defaultUserValues(User user) {
//        user.setActiveFlag(true);

        if(user.getCompany().getId() == null) {
            try {
                user.getCompany().setId(UUID.fromString(user.getCompany().getIdFromHref()));
            }
            catch (IllegalArgumentException e) {
                log.warn("{} is invalid UUID", user.getCompany().getIdFromHref());
                throw new BadRequestException(generateBadUUIDException());
            }
        }
    }

    private OFSErrors generateBadUUIDException() {
        OFSErrors ofsErrors = new OFSErrors();
        ofsErrors.rejectValue("user.company.id.invalid", "company.id", "Invalid company id. Company does not exits.");
        return ofsErrors;
    }

    private String getValidAuthHeader(HttpHeaders headers) {

        List<String> authHeader = headers.get("Authorization");
        if(!validateAuthorizationHeader(authHeader)) {
            throw new ForbiddenException();
        }

        String authString = headers.get("Authorization").get(0);
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

    private String getBeaerTokenFromAuthentication(String authString) {
        String[] authentication = authString.split(" ");

        if(!authentication[0].equalsIgnoreCase("Bearer")) {
            throw new ForbiddenException();
        }

        return authentication[1];
    }
}
