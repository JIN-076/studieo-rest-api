package org.dev.course.studieo.domain.user.entity.vo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Tel {

    private final String tel;

    public Tel(String tel) {
        this.tel = tel;
    }

    public static Tel valueOf(String tel) {
        return new Tel(tel);
    }
}
