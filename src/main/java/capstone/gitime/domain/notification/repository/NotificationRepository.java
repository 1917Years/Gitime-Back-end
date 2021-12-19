package capstone.gitime.domain.notification.repository;

import capstone.gitime.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select n from MemberNotification mn join mn.member m join mn.notification n where m.id=:memberId")
    List<Notification> findNotificationByMemberId(@Param("memberId") Long memberId);

}
