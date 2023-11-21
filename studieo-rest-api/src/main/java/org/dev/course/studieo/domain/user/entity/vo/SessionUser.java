package org.dev.course.studieo.domain.user.entity.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class SessionUser {
    private UUID userId;
    private String loginId;
}
