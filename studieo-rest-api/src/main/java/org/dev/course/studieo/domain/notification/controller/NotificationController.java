package org.dev.course.studieo.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.notification.dto.NotificationResponse;
import org.dev.course.studieo.domain.notification.entity.Notification;
import org.dev.course.studieo.domain.notification.entity.NotificationDto;
import org.dev.course.studieo.domain.notification.mapper.NotificationMapper;
import org.dev.course.studieo.domain.notification.service.NotificationService;
import org.dev.course.studieo.domain.participant.dto.ParticipantDto;
import org.dev.course.studieo.domain.participant.entity.Participant;
import org.dev.course.studieo.domain.participant.entity.RoleType;
import org.dev.course.studieo.domain.participant.mapper.ParticipantMapper;
import org.dev.course.studieo.domain.participant.service.ParticipantService;
import org.dev.course.studieo.domain.study.entity.CategoryType;
import org.dev.course.studieo.domain.study.entity.Study;
import org.dev.course.studieo.domain.study.service.StudyService;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.entity.vo.SessionUser;
import org.dev.course.studieo.domain.user.service.UserService;
import org.dev.course.studieo.util.exception.UnauthorizedAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final StudyService studyService;
    private final UserService userService;
    private final ParticipantService participantService;

    // 서버는 EventSource 통해 날아오는 요청을 처리할 컨트롤러가 필요
    // 클라이언트가 /notification/{studyId}로 연결을 맺음
    // 클라이언트가 신청 메시지를 작성해서 서버에게 보내는 POST 요청은 SSE 통신과 상관X
    // 서버가 /notification/{studyId}로 클라이언트와 연결을 맺고 데이터를 전송하면 클라이언트가 이를 받아서 띄움
    @GetMapping(value = "/notification", produces = "text/event-stream")
    public SseEmitter connect(SessionUser user,
                              @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.connect(user.getUserId(), lastEventId);
    }

    @PostMapping(value = "/studies/{studyId}/apply")
    public ResponseEntity<Notification> sendNotification(SessionUser user,
                                                         @RequestParam String message,
                                                         @PathVariable UUID studyId,
                                                         @RequestHeader(value = "Last-Event-ID", required = false,
                                                                        defaultValue = "") String lastEventId) {

        ParticipantDto dto = ParticipantMapper.INSTANCE.mapToDtoWithStudyId(studyId);
        Participant participant = participantService.findLeaderById(dto);
        Notification notification = notificationService.send(user, studyId, participant.getUserId(), message);
        URI location = URI.create("/studies/" + studyId + "/participate");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }

    @GetMapping("/api/notifications")
    public List<Notification> getNotification(SessionUser user) {
        NotificationDto notificationDto = NotificationMapper.INSTANCE.mapToDto(user.getUserId());
        return notificationService.findAllNotificationByUserId(notificationDto);
    }
}
