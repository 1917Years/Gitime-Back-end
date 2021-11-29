package capstone.gitime.domain.memberteam.repository;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberTeamRepository extends JpaRepository<MemberTeam,Long> {

    @Query(value = "select mt from MemberTeam mt join fetch mt.member m join fetch mt.team t " +
            "join fetch t.gitRepo g where m.id=:memberId and mt.teamInvite = :teamInvite",
            countQuery = "select mt.id from MemberTeam mt where mt.member.id=:memberId")
    Page<MemberTeam> findLazyListPageByIdAndAccept(@Param("memberId") Long memberId, Pageable pageable, @Param("teamMemberStatus") TeamMemberStatus teamMemberStatus);

    @Query(value = "select mt from MemberTeam mt where mt.member = :memberId and mt.team = :teamId")
    Optional<MemberTeam> findByTeamAndMember(@Param("memberId") Long memberId, @Param("teamId") Long teamId);

    @Query(value = "select mt from MemberTeam mt join mt.team t where mt.member.id = :memberId and t.teamName = :teamName")
    Optional<MemberTeam> findByTeamNameAndMember(@Param("memberId") Long memberId, @Param("teamName") String teamName);

    @Query(value = "select df.field from MemberTeam mt join mt.developField df where mt.member = :memberId and mt.team = :teamId")
    String findFieldByTeamAndMember(@Param("memberId") Long memberId, @Param("teamId") Long teamId);
}
