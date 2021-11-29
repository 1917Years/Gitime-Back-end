package capstone.gitime.domain.common.repository;

import capstone.gitime.domain.common.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {

    @Query(value = "select gr from GitRepo gr join gr.member m on m.id=:memberId where gr.url=:url")
    Optional<GitRepo> findByUrl(@Param("url") String url, @Param("memberId") Long memberId);

    @Query(value = "select gr from GitRepo gr where gr.member.id=:memberId")
    List<GitRepo> findListBy(@Param("memberId") Long memberId);
}
