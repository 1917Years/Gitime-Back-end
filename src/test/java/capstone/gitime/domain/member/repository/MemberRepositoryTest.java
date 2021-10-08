package capstone.gitime.domain.member.repository;

import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.entity.OwnMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        Member member = OwnMember.createOwnMemberEntity()
                .userName("memberA")
                .birth(LocalDate.now())
                .build();
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow();

        assertThat(findMember).isEqualTo(member);

    }

}