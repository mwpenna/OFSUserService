package com.ofs.validations.user;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.form.update.ChangeSet;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserCreateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateUsernameDNE implements UserCreateValidation {

    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(User user, OFSErrors errors) throws Exception {
        Optional optionalUser = userRepository.getUserByUserName(user.getUserName());

        if(optionalUser.isPresent()) {
            errors.rejectValue("user.username.exists", "user.username", "Invalid username. Username already exists.");
            return;
        }
    }

    @Override
    public void validate(ChangeSet changeSet, User user, OFSErrors errors) throws Exception {
        validate(user, errors);
    }
}
