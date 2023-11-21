package org.dev.course.studieo.domain.participant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.notification.entity.NotificationDto;
import org.dev.course.studieo.domain.notification.mapper.NotificationMapper;
import org.dev.course.studieo.domain.notification.service.NotificationService;
import org.dev.course.studieo.domain.participant.dto.ParticipantDto;
import org.dev.course.studieo.domain.participant.entity.Participant;
import org.dev.course.studieo.domain.participant.mapper.ParticipantMapper;
import org.dev.course.studieo.domain.participant.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/participation")
@RequiredArgsConstructor
public class ParticipantApiController {

    private final ParticipantService participantService;
    private final NotificationService notificationService;

    @PostMapping("/accept-participate")
    public ResponseEntity<String> acceptNotification(
            @RequestBody Map<String, UUID> requestBody) {

        UUID userId = requestBody.get("userId");
        UUID studyId = requestBody.get("studyId");

        ParticipantDto participantDto = ParticipantMapper.INSTANCE.mapToDto(studyId, userId);
        Participant participant = participantService.findById(participantDto);
        participant.setMember();
        Participant participatedUser = participantService.acceptParticipation(participant);
        NotificationDto notificationDto = NotificationMapper.INSTANCE.mapToDtoWithId(studyId, userId);
        notificationService.delete(notificationDto);
        return ResponseEntity.ok().body("success");
    }
}
