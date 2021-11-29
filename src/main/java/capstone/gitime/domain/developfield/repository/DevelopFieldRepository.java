package capstone.gitime.domain.developfield.repository;

import capstone.gitime.domain.developfield.entity.DevelopField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DevelopFieldRepository extends JpaRepository<DevelopField, Long> {

    @Query(value = "select df from DevelopField df join df.team t on t.teamName=:teamName where df.field=:field")
    Optional<DevelopField> findByField(@Param("field") String field, @Param("teamName") String teamName);


}
