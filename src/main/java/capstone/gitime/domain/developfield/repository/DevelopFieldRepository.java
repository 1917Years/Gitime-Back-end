package capstone.gitime.domain.developfield.repository;

import capstone.gitime.domain.developfield.entity.DevelopField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevelopFieldRepository extends JpaRepository<DevelopField, Long> {

    Optional<DevelopField> findByField(String field);
}
