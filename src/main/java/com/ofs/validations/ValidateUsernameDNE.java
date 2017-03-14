package com.ofs.validations;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.model.OFSErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateUsernameDNE implements UserCreateValidation{

    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(User user, OFSErrors errors) {
        Optional optionalUser = userRepository.getUserByUserName(user.getEmailAddress());

        if(optionalUser.isPresent()) {
            errors.rejectValue("user.username.exists", "user.username", "Invalid username. Username already exists.");
            return;
        }
    }
}
