package capstone.gitime.domain.team.repository;

import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query(value = "select df from DevelopField df join df.team t where t.id = :id")
    List<DevelopField> findAllById(@Param("id") Long teamId);
}
