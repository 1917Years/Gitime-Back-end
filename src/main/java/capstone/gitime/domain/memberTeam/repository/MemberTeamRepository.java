package capstone.gitime.domain.memberTeam.repository;

import capstone.gitime.domain.memberTeam.entity.MemberTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberTeamRepository extends JpaRepository<MemberTeam,Long> {

    @Query(value = "select mt from MemberTeam mt join fetch mt.member m join fetch mt.team t " +
            "join fetch t.gitRepo g where m.id=:memberId",
    countQuery = "select mt from MemberTeam mt where mt.member.id=:memberId")
    Page<MemberTeam> findLazyListPageById(@Param("memberId") Long memberId, Pageable pageable);
}
