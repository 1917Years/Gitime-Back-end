package capstone.gitime.api.controller.dto;

import capstone.gitime.domain.notification.entity.NotificationImportance;
import lombok.Data;

@Data
public class NotificationRequestDto {

    private String message;
    private NotificationImportance notificationImportance;
}
