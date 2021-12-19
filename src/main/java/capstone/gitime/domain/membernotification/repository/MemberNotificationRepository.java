package capstone.gitime.domain.membernotification.repository;

import capstone.gitime.domain.membernotification.entity.MemberNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {

}
