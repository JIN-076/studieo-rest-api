package org.dev.course.studieo.domain.study.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.participant.dto.ParticipantDto;
import org.dev.course.studieo.domain.participant.entity.Participant;
import org.dev.course.studieo.domain.participant.entity.RoleType;
import org.dev.course.studieo.domain.participant.mapper.ParticipantMapper;
import org.dev.course.studieo.domain.participant.service.ParticipantService;
import org.dev.course.studieo.domain.study.dto.StudyDto;
import org.dev.course.studieo.domain.study.dto.StudySaveRequestDto;
import org.dev.course.studieo.domain.study.entity.CategoryType;
import org.dev.course.studieo.domain.study.entity.Study;
import org.dev.course.studieo.domain.study.entity.Study.Pair;
import org.dev.course.studieo.domain.study.entity.StudyType;
import org.dev.course.studieo.domain.study.mapper.StudyMapper;
import org.dev.course.studieo.domain.study.service.StudyService;
import org.dev.course.studieo.domain.user.dto.UserGetRequestDto;
import org.dev.course.studieo.domain.user.entity.vo.SessionUser;
import org.dev.course.studieo.domain.user.mapper.UserMapper;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final ParticipantService participantService;

    @GetMapping("/createForm")
    public String createForm(Model model) {
        model.addAttribute("studySaveRequestDto", StudySaveRequestDto.builder().build());
        return "studies/saveForm";
    }

    @PostMapping("/create")
    public String createStudy(SessionUser user,
                              @Validated @ModelAttribute StudySaveRequestDto studySaveRequestDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            log.error("errors={}", bindingResult);
            return "studies/saveForm";
        }

        Pair pair = getCoordinate(studySaveRequestDto);
        StudyDto studyDto = StudyMapper.INSTANCE.mapToDto(studySaveRequestDto, pair.getX(), pair.getY());
        Study savedStudy = studyService.save(studyDto);

        ParticipantDto participantDto = ParticipantMapper.INSTANCE.mapToDto(savedStudy.getStudyId(), user.getUserId());
        Participant savedParticipants = participantService.createStudy(participantDto);

        model.addAttribute("study", savedStudy);
        model.addAttribute("role", RoleType.valueOf(savedParticipants.getRole().getValue()));
        model.addAttribute("participant", savedParticipants);
        model.addAttribute("category", CategoryType.valueOf(savedStudy.getCategory().getValue()));

        return "studies/detailPage";
    }

    @GetMapping()
    public String listAllStudies(Model model) {
        List<Study> studies = studyService.findAll();
        model.addAttribute("studies", studies);
        return "studies/list";
    }

    @GetMapping("/{studyId}")
    public String list(SessionUser user,
                       @PathVariable UUID studyId,
                       Model model) {

        Study study = studyService.findById(studyId);
        ParticipantDto participantDto = ParticipantMapper.INSTANCE.mapToDto(studyId, user.getUserId());
        Participant participant = getProperParticipant(participantDto);

        model.addAttribute("study", study);
        model.addAttribute("role", RoleType.valueOf(participant.getRole().getValue()));
        model.addAttribute("category", CategoryType.valueOf(study.getCategory().getValue()));
        return "studies/detailPage";
    }

    @GetMapping("/{studyId}/edit")
    public String editForm(@PathVariable UUID studyId,
                           HttpServletRequest httpServletRequest,
                           Model model) {

        Study study = studyService.findById(studyId);
        String savedLoginId = (String) httpServletRequest.getSession(false).getAttribute("loginId");
        UserGetRequestDto getRequestDto = UserMapper.INSTANCE.mapToDtoWithId(savedLoginId);

        model.addAttribute("study", study);
        return "studies/editDetailPage";
    }

    @PutMapping("/{studyId}")
    public String edit(SessionUser user,
                       @PathVariable UUID studyId,
                       Model model) {

        Study study = studyService.findById(studyId);
        ParticipantDto participantDto = ParticipantMapper.INSTANCE.mapToDto(studyId, user.getUserId());
        Participant participant = getProperParticipant(participantDto);

        model.addAttribute("study", study);
        model.addAttribute("role", RoleType.valueOf(participant.getRole().getValue()));
        model.addAttribute("category", CategoryType.valueOf(study.getCategory().getValue()));

        return "studies/detailPage";
    }

    // 사용자가 detailPage 페이지에서
    // 수락이 필요없는 '참가하기' 누르면 GET /studies/{studyId}/apply 매핑에 걸림
    // 수락이 필요한 '참가하기' 누르면 새로운 url 매핑 없이 모달을 띄움 -> '신청하기' 누르면 POST /studies/{studyId}/apply 매핑에 걸림
    @GetMapping("/{studyId}/participate")
    public String attendStudy(SessionUser user,
                              @PathVariable UUID studyId,
                              Model model) {

        Study study = studyService.findById(studyId);
        ParticipantDto participantDto = getDtoWithRole(study, user.getUserId());
        Participant participant = participantService.participateStudy(participantDto);

        model.addAttribute("study", study);
        model.addAttribute("category", CategoryType.valueOf(study.getCategory().getValue()));
        model.addAttribute("role", RoleType.valueOf(participant.getRole().getValue()));

        if (!study.isRequireAccept()) {
            return "redirect:/studies/{studyId}";
        }
        return "studies/detailPage";
    }

    private ParticipantDto getDtoWithRole(Study study, UUID userId) {
        if (study.isRequireAccept()) {
            return ParticipantMapper.INSTANCE.mapToDtoWithRole(study.getStudyId(), userId, RoleType.TEAM_APPLICANT);
        } else {
            return ParticipantMapper.INSTANCE.mapToDtoWithRole(study.getStudyId(), userId, RoleType.TEAM_MEMBER);
        }
    }

    private Participant getProperParticipant(ParticipantDto participantDto) {
        if (participantService.isExist(participantDto)) {
            return participantService.findById(participantDto);
        } else {
            return ParticipantMapper.INSTANCE.mapToEntity(participantDto, RoleType.NONE);
        }
    }

    private static Pair getCoordinate(StudySaveRequestDto studiesSaveRequestDto) {

        if (!studiesSaveRequestDto.isEqual(StudyType.OFFLINE)) {
            return new Pair(null, null);
        }

        String clientId = "5ucfso76ve";
        String clientSecret = "jA7c615AYmtZlnZJRqB3KtmKWjz8Uj6j4wVu9lDl";
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder stringBuilder = new StringBuilder();
        String url = stringBuilder.append(apiUrl)
                .append("?query=")
                .append(studiesSaveRequestDto.getAddress())
                .toString();

        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                JSONObject.class
        );

        JSONObject responseBody = responseEntity.getBody();
        if (responseBody != null) {
            ArrayList<LinkedHashMap<String, String>> list = (ArrayList<LinkedHashMap<String, String>>) responseBody.get("addresses");
            LinkedHashMap<String, String> addresses = list.get(0);
            return new Pair(Double.parseDouble(addresses.get("x")), Double.parseDouble(addresses.get("y")));
        }
        return new Pair(null, null);
    }
}
