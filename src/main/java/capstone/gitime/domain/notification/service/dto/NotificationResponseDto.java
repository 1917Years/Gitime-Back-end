package capstone.gitime.domain.notification.service.dto;

import capstone.gitime.domain.membernotification.entity.MemberNotification;
import capstone.gitime.domain.notification.entity.Notification;
import capstone.gitime.domain.notification.entity.NotificationImportance;
import capstone.gitime.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponseDto {

    private String message;
    private NotificationImportance notificationImportance;
    private NotificationType notificationType;

    public static NotificationResponseDto of(Notification notification) {
        return new NotificationResponseDto(notification.getMessage(),
                notification.getNotificationImportance(),
                notification.getNotificationType());
    }
}
