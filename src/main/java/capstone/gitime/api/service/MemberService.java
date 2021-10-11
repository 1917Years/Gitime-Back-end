package capstone.gitime.api.service;

import capstone.gitime.api.controller.dto.MemberModifyRequestDto;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.service.dto.MemberInfoResponseDto;
import capstone.gitime.api.service.dto.TeamInfoResponseDto;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.memberTeam.repository.MemberTeamRepository;
import capstone.gitime.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member findMember = getMember(memberId);

        return new MemberInfoResponseDto(findMember);
    }

    public void modifyMemberInfo(Long memberId, MemberModifyRequestDto requestDto) {
        Member findMember = getMember(memberId);
        findMember.updateInfo(requestDto,passwordEncoder);
    }

    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("멤버 조회에 실패하였습니다. PK값 확인 불가"));
    }



}
