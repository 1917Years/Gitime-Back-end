package capstone.gitime.domain.membernotification.entity;

import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.notification.entity.Notification;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_notification_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_notification_member_fk"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", foreignKey = @ForeignKey(name = "member_notification_notification_fk"))
    private Notification notification;


    @Builder(builderMethodName = "createMemberNotification")
    public MemberNotification(Member member, Notification notification) {
        this.member = member;
        this.notification = notification;
    }

}
