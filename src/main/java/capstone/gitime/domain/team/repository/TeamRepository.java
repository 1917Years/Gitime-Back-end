package capstone.gitime.domain.team.repository;

import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query(value = "select df from DevelopField df join df.team t where t.id = :id")
    List<DevelopField> findAllById(@Param("id") Long teamId);

    @Query(value = "select t from Team t where t.teamName=:teamName")
    Optional<Team> findNoticeByName(@Param("teamName") String teamName);

    @Query(value = "select t from Team t where t.teamName = :teamName")
    Optional<Team> findTeamByName(@Param("teamName") String teamName);

    @Query(value = "select count(mt) from MemberTeam mt join mt.team t where t.teamName = :teamName")
    Integer countAllByTeamName(@Param("teamName") String teamName);

    @Query(value = "select df from DevelopField df join df.team t on t.teamName=:teamName")
    List<DevelopField> findDevelopFieldByTeamName(@Param("teamName") String teamName);

    @Query(value = "select count(mt) from MemberTeam mt join mt.team t where t.teamName=:teamName")
    Integer findTeamMemberCountByTeamName(@Param("teamName") String teamName);

}
