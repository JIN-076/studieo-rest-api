package org.dev.course.studieo.util.converter;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptConverter {

    private BCryptConverter() {}

    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
