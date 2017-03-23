package com.ofs.validations;

import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.model.OFSErrors;
import com.ofs.validations.user.ValidateUsernameDNE;
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

public class ValidateUsernameDNETest {

    @InjectMocks
    ValidateUsernameDNE objectUnderTest;

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
    }

    @Test
    public void validateUserNamePresent_shouldAddErrorToErrorsList() throws Exception {
        OFSErrors errors = new OFSErrors();

        when(userRepository.getUserByUserName(any())).thenReturn(Optional.of(user));

        objectUnderTest.validate(user, errors);
        assertFalse(errors.isEmpty());

        errors.forEach(ofsError -> {
            assertEquals(ofsError.getCode(), "user.username.exists");
            assertEquals(ofsError.getProperties().get("field"),"user.username");
            assertEquals(ofsError.getDeveloperMessage(), "Invalid username. Username already exists.");
        });
    }

    @Test
    public void validateUserNameNotPresent_shouldNotAddErrorToList() throws Exception {
        OFSErrors errors = new OFSErrors();

        when(userRepository.getUserByUserName(any())).thenReturn(Optional.empty());

        objectUnderTest.validate(user, errors);
        assertTrue(errors.isEmpty());
    }

}