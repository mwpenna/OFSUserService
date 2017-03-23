package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserCreateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateEmailDNE implements UserCreateValidation {

    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Optional optionalUser = userRepository.getUserByEmailAddress(user.getEmailAddress());

        if(optionalUser.isPresent()) {
            errors.rejectValue("user.emailaddress.exists", "user.emailaddress", "Invalid email address. Email address already exists.");
            return;
        }

    }
}
