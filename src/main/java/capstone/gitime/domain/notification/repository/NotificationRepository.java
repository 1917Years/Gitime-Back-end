package capstone.gitime.domain.notification.repository;

import capstone.gitime.domain.notification.entity.Notification;
import capstone.gitime.domain.notification.service.dto.NotificationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query(value = "select n from Notification n join fetch n.memberTeam mt join mt.member m join mt.team t where m.id=:memberId",
    countQuery = "select count(n) from Notification n join n.memberTeam mt where mt.member=:memberId")
    Page<Notification> getAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);


    @Query(value = "select n from Notification n join fetch n.memberTeam mt join mt.member m join mt.team t where t.teamName=:teamName",
            countQuery = "select count(n) from Notification n join n.memberTeam mt join mt.team t where t.teamName=:teamName")
    Page<Notification> getLatestByTeamName(@Param("teamName") String teamName, Pageable pageable);

    @Query(value = "select n from Notification n join fetch n.memberTeam mt join mt.member m join mt.team t where t.teamName=:teamName and n.notificationType='TODO'",
            countQuery = "select count(n) from Notification n join n.memberTeam mt join mt.team t where t.teamName=:teamName and n.notificationType='TODO'")
    Page<Notification> getUpcomingByTeamName(@Param("teamName") String teamName,
                                             Pageable pageable);


}
