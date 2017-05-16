package com.ofs.resolver;

import com.ofs.models.User;
import com.ofs.server.filter.Filter;
import com.ofs.server.filter.views.Public;
import com.ofs.server.filter.views.SystemAdmin;
import com.ofs.server.security.Subject;
import org.springframework.stereotype.Component;

@Component(value = "userResolver")
public class UserResolver implements Filter<User>{

    @Override
    public Class<? extends Public> filterView(User user, Subject subject) {
        if(subject.getRole().equalsIgnoreCase(User.Role.SYSTEM_ADMIN.toString())) {
            return SystemAdmin.class;
        }

        return Public.class;
    }
}
