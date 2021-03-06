package capstone.gitime.domain.memberteam.repository;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamMemberStatus;
import capstone.gitime.domain.team.entity.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;


public interface MemberTeamRepository extends JpaRepository<MemberTeam,Long> {

    @Query(value = "select mt from MemberTeam mt join fetch mt.member m join fetch mt.team t " +
            "join fetch t.gitRepo g where m.id=:memberId and mt.teamMemberStatus = 'ACCEPT' and " +
            "t.teamStatus='ACTIVE'",
            countQuery = "select count(mt) from MemberTeam mt where mt.member.id=:memberId")
    Page<MemberTeam> findLazyListPageByIdAndAccept(@Param("memberId") Long memberId, Pageable pageable);

    @Query(value = "select mt from MemberTeam mt join fetch mt.member m join mt.team t where t.teamName=:teamName",
            countQuery = "select count(mt) from MemberTeam mt join mt.team t where t.teamName=:teamName")
    Page<MemberTeam> findLazyListPageByTeamName(@Param("teamName") String teamName,
                                                         Pageable pageable);

    @Query(value = "select mt from MemberTeam mt join fetch mt.member m join mt.team t where mt.teamMemberStatus='ACCEPT' and t.teamName=:teamName")
    List<MemberTeam> findByAcceptAndTeamName(@Param("teamName") String teamName);

    @Query(value = "select mt from MemberTeam mt join fetch mt.team t join mt.member m where m.id=:memberId and mt.teamMemberStatus ='WAIT'")
    List<MemberTeam> findAllByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select mt from MemberTeam mt join mt.team t where mt.member.id = :memberId and t.teamName = :teamName")
    Optional<MemberTeam> findByTeamNameAndMember(@Param("memberId") Long memberId, @Param("teamName") String teamName);

    @Query(value = "select mt from MemberTeam mt join fetch mt.team t where mt.member.id = :memberId and t.teamName = :teamName")
    Optional<MemberTeam> findFetchTeamByTeamNameAndMember(@Param("memberId") Long memberId, @Param("teamName") String teamName);

    @Query(value = "select mt from MemberTeam mt join mt.team t join mt.member m where m.email = :memberEmail and t.teamName = :teamName")
    Optional<MemberTeam> findByTeamNameAndMemberEmail(@Param("memberEmail") String memberEmail, @Param("teamName") String teamName);

    @Query(value = "select mt from MemberTeam mt join mt.team t join fetch mt.developField df where mt.member.id = :memberId and t.teamName = :teamName")
    Optional<MemberTeam> findFetchDevelopFieldByTeamNameAndMember(@Param("memberId") Long memberId, @Param("teamName") String teamName);

    @Query(value = "select df.field from MemberTeam mt join mt.developField df where mt.member = :memberId and mt.team = :teamId")
    String findFieldByTeamAndMember(@Param("memberId") Long memberId, @Param("teamId") Long teamId);

    @Query(value = "select mt from MemberTeam mt join mt.team t join mt.member m where m.id=:memberId and mt.acceptCode=:acceptCode")
    Optional<MemberTeam> findByMemberAndAcceptCode(@Param("memberId") Long memberId, @Param("acceptCode") String acceptCode);
}
