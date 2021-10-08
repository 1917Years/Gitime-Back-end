package capstone.gitime.domain.common.repository;

import capstone.gitime.domain.common.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {

}
