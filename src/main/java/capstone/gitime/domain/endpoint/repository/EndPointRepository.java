package capstone.gitime.domain.endpoint.repository;

import capstone.gitime.domain.endpoint.entity.EndPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndPointRepository extends JpaRepository<EndPoint, Long> {
}
