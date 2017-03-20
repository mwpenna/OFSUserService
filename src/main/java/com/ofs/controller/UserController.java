package com.ofs.controller;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.OFSController;
import com.ofs.server.OFSServerId;
import com.ofs.server.form.OFSServerForm;
import com.ofs.server.form.ValidationSchema;
import com.ofs.server.model.OFSErrors;
import com.ofs.validators.UserCreateValidator;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@OFSController
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCreateValidator userCreateValidator;

    @ValidationSchema(value = "/user-create.json")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@OFSServerId URI id, OFSServerForm<User> form) throws IOException, Exception{
        User user = form.create(id);
        defaultUserValues(user);
        encryptPassword(user);

        OFSErrors errors = new OFSErrors();
        userCreateValidator.validate(user, errors);

        userRepository.addUser(user);
        return ResponseEntity.created(id).build();
    }

    @PostMapping(value = "/id/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(String id, User user) { return ResponseEntity.noContent().build();}

    @DeleteMapping(value = "/id/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(String id) { return ResponseEntity.noContent().build();}

    @GetMapping(value="/getToken")
    public ResponseEntity getToken() { return ResponseEntity.ok("token here");}

    @GetMapping(value = "/authenticate")
    public ResponseEntity authenticateUser() { return ResponseEntity.noContent().build();}

    private void encryptPassword(User user) {
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = strongPasswordEncryptor.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    private void defaultUserValues(User user) {
        user.setId(UUID.fromString(user.getIdFromHref()));
        user.setActiveFlag(true);
    }
}
