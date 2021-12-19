package capstone.gitime.domain.notification.service;

import capstone.gitime.api.controller.dto.NotificationRequestDto;
import capstone.gitime.api.exception.exception.global.NotAccessException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.domain.membernotification.entity.MemberNotification;
import capstone.gitime.domain.membernotification.repository.MemberNotificationRepository;
import capstone.gitime.domain.memberteam.entity.TeamAuthority;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import capstone.gitime.domain.notification.entity.Notification;
import capstone.gitime.domain.notification.entity.NotificationType;
import capstone.gitime.domain.notification.repository.NotificationRepository;
import capstone.gitime.domain.notification.service.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MemberNotificationRepository memberNotificationRepository;
    private final NotificationRepository notificationRepository;
    private final MemberTeamRepository memberTeamRepository;


    // ADMIN ONLY
    @Transactional
    public void alertAllMember(Long memberId, NotificationRequestDto requestDto) {

    }

    // ROLE_PARENT ONLY
    @Transactional
    public void alertAllTeamMember(Long memberId, String teamName, NotificationRequestDto requestDto) {

        if (!memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException())
                .getTeamAuthority().equals(TeamAuthority.ROLE_PARENT)) {
            throw new NotAccessException("권한이 없습니다.");
        }

        Notification newNotification = Notification.createNotification()
                .message(requestDto.getMessage())
                .notificationImportance(requestDto.getNotificationImportance())
                .notificationType(NotificationType.TEAM_MEMBER)
                .build();

        notificationRepository.save(newNotification);

        List<MemberNotification> memberNotificationList = new ArrayList<>();

        memberTeamRepository.findFetchMemberByTeamNameAndMember(teamName)
                .stream().forEach((item) -> {
                    memberNotificationList.add(MemberNotification.createMemberNotification()
                            .member(item.getMember())
                            .notification(newNotification)
                            .build());
                });

        memberNotificationRepository.saveAll(memberNotificationList);
    }

    public List<NotificationResponseDto> getAllNotification(Long memberId) {

        return notificationRepository.findNotificationByMemberId(memberId)
                .stream()
                .map(NotificationResponseDto::of)
                .collect(Collectors.toList());

    }

}
