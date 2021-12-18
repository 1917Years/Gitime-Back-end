package capstone.gitime.domain.member.repository;

import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.dto.IdPasswordAuthDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "select m.password from Member m where m.email=:email")
    String findPasswordByEmail(@Param("email") String email);

    @Query(value = "select m from Member m where m.email=:email")
    Optional<Member> findMemberByEmail(@Param("email") String email);

}
