package capstone.gitime.domain.endpoint.repository;

import capstone.gitime.domain.endpoint.entity.EndPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EndPointRepository extends JpaRepository<EndPoint, Long> {

    @Query(value = "select e from EndPoint e join e.team t where t.teamName=:teamName")
    Optional<EndPoint> findByTeamName(@Param("teamName") String teamName);
}
