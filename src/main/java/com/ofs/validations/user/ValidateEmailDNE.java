package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserCreateValidation;
import com.ofs.validations.UserUpdateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateEmailDNE implements UserCreateValidation, UserUpdateValidation {

    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Optional optionalUser = userRepository.getUserByEmailAddress(user.getEmailAddress());

        if(optionalUser.isPresent()) {
            errors.rejectValue("user.emailaddress.exists", "user.emailaddress", "Invalid email address. Email address already exists.");
        }

    }

    @Override
    public void validate(ChangeSet changeSet, User user, OFSErrors errors) throws Exception {
        if(changeSet.contains("emailAddress")) {
            validate(user, errors);
        }
    }
}
