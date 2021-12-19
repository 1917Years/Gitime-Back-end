package capstone.gitime.domain.notification.service;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.notification.entity.Notification;
import capstone.gitime.domain.notification.entity.NotificationImportance;
import capstone.gitime.domain.notification.entity.NotificationType;
import capstone.gitime.domain.notification.repository.NotificationRepository;
import capstone.gitime.domain.notification.service.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void pushNotificationToTeam(MemberTeam memberTeam, NotificationType notificationType, NotificationImportance notificationImportance,
                                       LocalDate untilDate) {

        Notification newNotification = Notification.createNotification()
                .memberTeam(memberTeam)
                .notificationType(notificationType)
                .notificationImportance(notificationImportance)
                .untilDate(untilDate)
                .build();

        notificationRepository.save(newNotification);
    }

    public List<NotificationResponseDto> getLatestNotificationByMember(Long memberId) {
        return notificationRepository.getAllByMemberId(memberId, PageRequest.of(0, 3, Sort.Direction.DESC, "createdAt"))
                .map(NotificationResponseDto::of)
                .getContent();
    }

    public List<NotificationResponseDto> getLatestNotificationByTeamName(String teamName) {
        return notificationRepository.getLatestByTeamName(teamName, PageRequest.of(0, 3, Sort.Direction.DESC, "createdAt"))
                .map(NotificationResponseDto::of)
                .getContent();
    }

    public List<NotificationResponseDto> getUpcomingNotificationByTeamName(String teamName) {
        return notificationRepository.getUpcomingByTeamName(teamName, PageRequest.of(0, 3, Sort.Direction.DESC, "createdAt"))
                .map(NotificationResponseDto::of)
                .getContent();
    }


}
