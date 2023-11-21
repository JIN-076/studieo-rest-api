package org.dev.course.studieo.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import static org.dev.course.studieo.constants.Constants.SESSION_KEY;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApplicationController {

    @GetMapping()
    public String welcome(@SessionAttribute(name = SESSION_KEY, required = false) String loginId) {
        if (loginId == null) return "home";
        return "user/homeAfterLoginWithAlert";
    }
}
