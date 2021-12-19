package capstone.gitime.domain.notification.service.dto;

import capstone.gitime.domain.notification.entity.Notification;
import capstone.gitime.domain.notification.entity.NotificationImportance;
import capstone.gitime.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NotificationResponseDto {

    private String teamName;
    private String memberName;
    private String message;
    private LocalDate untilDate;
    private NotificationImportance notificationImportance;
    private NotificationType notificationType;

    public static NotificationResponseDto of(Notification notification) {
        return new NotificationResponseDto(
                notification.getMemberTeam().getTeam().getTeamName(),
                notification.getMemberTeam().getMember().getUserName(),
                null,
                null,
                notification.getNotificationImportance(),
                notification.getNotificationType()
        );
    }

    public static NotificationResponseDto ofUpcoming(Notification notification, String message, LocalDate untilDate) {

        return new NotificationResponseDto(
                notification.getMemberTeam().getTeam().getTeamName(),
                notification.getMemberTeam().getMember().getUserName(),
                message, untilDate,
                notification.getNotificationImportance(),
                notification.getNotificationType()
        );
    }
}
