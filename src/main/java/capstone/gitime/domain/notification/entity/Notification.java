package capstone.gitime.domain.notification.entity;


import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private NotificationImportance notificationImportance;

    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @Builder(builderMethodName = "createNotification")
    public Notification(String message, NotificationImportance notificationImportance, NotificationType notificationType) {
        this.message = message;
        this.notificationImportance = notificationImportance;
        this.notificationType = notificationType;
    }


}
