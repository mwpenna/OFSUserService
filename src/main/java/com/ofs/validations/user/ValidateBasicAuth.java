package com.ofs.validations.user;

import com.ofs.models.BasicAuthUser;
import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.UserGetTokenValidation;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateBasicAuth implements UserGetTokenValidation {
    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(BasicAuthUser basicAuthUser, OFSErrors errors) throws Exception {
        Optional<User> userOptional = userRepository.getUserByUserName(basicAuthUser.getUserName());

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            if(!user.isActiveFlag()) {
                errors.rejectValue("user.active.false","User is no longer active. Please contact your company administrator.");
            }

            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            if(!passwordEncryptor.checkPassword(basicAuthUser.getPassword(), user.getPassword())) {
                errors.rejectValue("user.authentication.failed", "Username/Password is not valid. Please retry with correct credentials.");
            }
        }
        else {
            errors.rejectValue("user.authentication.failed", "Username/Password is not valid. Please retry with correct credentials.");
        }
    }
}
