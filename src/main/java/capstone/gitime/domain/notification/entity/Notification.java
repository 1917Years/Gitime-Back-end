package capstone.gitime.domain.notification.entity;


import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private NotificationImportance notificationImportance;

    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    private LocalDate untilDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_team_id", foreignKey = @ForeignKey(name = "notification_member_team_fk"))
    private MemberTeam memberTeam;

    @Builder(builderMethodName = "createNotification")
    public Notification(NotificationImportance notificationImportance, NotificationType notificationType, MemberTeam memberTeam, LocalDate untilDate) {
        this.notificationImportance = notificationImportance;
        this.notificationType = notificationType;
        this.untilDate = untilDate;
        this.memberTeam = memberTeam;
    }
}
