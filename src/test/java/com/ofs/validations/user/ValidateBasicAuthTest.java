package com.ofs.validations.user;

import com.ofs.models.BasicAuthUser;
import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.model.OFSErrors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ValidateBasicAuthTest {

    @InjectMocks
    ValidateBasicAuth objectUnderTest;

    @Mock
    UserRepository userRepository;

    private User user;
    private UUID id;

    @Before
    public void setup() {
        initMocks(this);

        id = UUID.randomUUID();
        user = new User();
        user.setUserName("someuser");
        user.setPassword("/x/sahoFY1NaZvnjfR5hb6vVb8azDOyRSv4gLZ5eFcMiGFEz4hFxtnYCPPsOhXHy");
    }

    @Test
    public void validateUserNotFound_shouldReturnAuthFailedError() throws Exception {
        when(userRepository.getUserByUserName(any())).thenReturn(Optional.empty());
        OFSErrors errors = new OFSErrors();
        objectUnderTest.validate(generateBasicAuthBadPassword(), errors);
        assertFalse(errors.isEmpty());
        assertEquals("user.authentication.failed", errors.getErrors().get(0).getCode());
        assertEquals("Username/Password is not valid. Please retry with correct credentials.", errors.getErrors().get(0).getDeveloperMessage());

    }

    @Test
    public void validateUserFoundUserInactive_shouldReturnError() throws Exception {
        user.setActiveFlag(false);
        when(userRepository.getUserByUserName(any())).thenReturn(Optional.of(user));
        OFSErrors errors = new OFSErrors();
        objectUnderTest.validate(generateBasicAuthBadPassword(), errors);
        assertFalse(errors.isEmpty());
        assertEquals("user.active.false", errors.getErrors().get(0).getCode());
        assertEquals("User is no longer active. Please contact your company administrator.", errors.getErrors().get(0).getDeveloperMessage());
    }

    @Test
    public void validateUserFoundPasswordWrong_shouldReturnAuthFailedError() throws Exception {
        user.setActiveFlag(true);
        when(userRepository.getUserByUserName(any())).thenReturn(Optional.of(user));
        OFSErrors errors = new OFSErrors();
        objectUnderTest.validate(generateBasicAuthBadPassword(), errors);
        assertFalse(errors.isEmpty());
        assertEquals("user.authentication.failed", errors.getErrors().get(0).getCode());
        assertEquals("Username/Password is not valid. Please retry with correct credentials.", errors.getErrors().get(0).getDeveloperMessage());
    }

    @Test
    public void validate_happyPath() throws Exception {
        user.setActiveFlag(true);
        when(userRepository.getUserByUserName(any())).thenReturn(Optional.of(user));
        OFSErrors errors = new OFSErrors();
        objectUnderTest.validate(generateBasicAuth(), errors);
        assertTrue(errors.isEmpty());
    }

    private BasicAuthUser generateBasicAuth() {
        return new BasicAuthUser("user", "password");
    }

    private BasicAuthUser generateBasicAuthBadPassword() {
        return new BasicAuthUser("user", "badpassword");
    }
}