package capstone.gitime.domain.memberteam.service;

import capstone.gitime.api.controller.dto.SetDevelopFieldRequestDto;
import capstone.gitime.api.exception.exception.global.NotFoundException;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.api.service.dto.TeamInfoResponseDto;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.developfield.repository.DevelopFieldRepository;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamAuthority;
import capstone.gitime.domain.memberteam.entity.TeamMemberStatus;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.entity.TeamStatus;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.team.service.dto.InviteTeamRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberTeamService {

    private final MemberRepository memberRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;
    private final DevelopFieldRepository developFieldRepository;

    public Page<TeamInfoResponseDto> getMemberTeamList(Long memberId, int page) {
        return memberTeamRepository.findLazyListPageByIdAndAccept(memberId, PageRequest.of(page, 5), TeamMemberStatus.ACCEPT, TeamStatus.ACTIVE)
                .map(TeamInfoResponseDto::new);
    }

    @Transactional
    public void inviteMemberToTeam(Long teamId, Long invitedMemberId) {
        Team findTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundTeamException());

        Member invitedMember = memberRepository.findById(invitedMemberId)
                .orElseThrow(() -> new NotFoundMemberException());

        MemberTeam newMemberTeam = MemberTeam.createMemberTeamEntity()
                .team(findTeam)
                .member(invitedMember)
                .teamMemberStatus(TeamMemberStatus.WAIT)
                .teamAuthority(TeamAuthority.ROLE_CHILD)
                .build();

        memberTeamRepository.save(newMemberTeam);
    }

    @Transactional
    public void modifyInviteTeamRequest(InviteTeamRequestDto requestDto, Long memberId, Long teamId) {
        if (requestDto.getRequest().equals("ACCEPT")) {

            memberTeamRepository.findByTeamAndMember(memberId, teamId)
                    .orElseThrow(() -> new NotFoundMemberTeamException())
                    .updateTeamInvite(TeamMemberStatus.ACCEPT);

        } else if (requestDto.getRequest().equals("DENIED")) {

            // DB 상에서 삭제하지 않는 이유는 나중에 팀 관리 페이지 멤버 초대쪽에서 로그로 보여주기 위함.
            memberTeamRepository.findByTeamAndMember(memberId, teamId)
                    .orElseThrow(() -> new NotFoundMemberTeamException())
                    .updateTeamInvite(TeamMemberStatus.DENIED);

        } else {
            throw new RuntimeException();
        }


    }

    @Transactional
    public void setDevelopFieldToMember(SetDevelopFieldRequestDto requestDto, Long memberId, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        DevelopField findDevelopField = developFieldRepository.findByField(requestDto.getDevelopField(), teamName)
                .orElseThrow(() -> new NotFoundException());

        findMemberTeam.updateDevelopField(findDevelopField);
    }

    public String getDevelopFieldFromMember(Long memberId, Long teamId) {
        return memberTeamRepository.findFieldByTeamAndMember(memberId, teamId);
    }

    @Transactional
    public void deleteMemberTeam(Long memberId, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        memberTeamRepository.delete(findMemberTeam);
    }
}
