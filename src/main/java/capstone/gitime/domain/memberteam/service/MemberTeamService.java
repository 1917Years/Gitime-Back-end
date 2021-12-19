package capstone.gitime.domain.memberteam.service;

import capstone.gitime.api.controller.dto.CreateInviteRequestDto;
import capstone.gitime.api.controller.dto.SetDevelopFieldRequestDto;
import capstone.gitime.api.exception.exception.global.NotAccessException;
import capstone.gitime.api.exception.exception.global.NotFoundException;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.exception.exception.memberteam.DuplicateMemberTeamException;
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
import capstone.gitime.domain.memberteam.service.dto.InvitationListRequestDto;
import capstone.gitime.domain.memberteam.service.dto.InviteMemberListRequestDto;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.entity.TeamStatus;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.team.service.dto.InviteTeamRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public Page<InviteMemberListRequestDto> getMemberTeamInviteList(String teamName, Integer page) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.Direction.DESC, "createdAt");

        return memberTeamRepository.findLazyListPageByTeamName(teamName, pageRequest)
                .map(InviteMemberListRequestDto::of);
    }

    @Transactional
    public void inviteMemberToTeam(String teamName, CreateInviteRequestDto requestDto) {

        memberTeamRepository.findByTeamNameAndMemberEmail(requestDto.getMemberEmail(),
                teamName).orElseThrow(() -> new DuplicateMemberTeamException());


        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        Member invitedMember = memberRepository.findByEmail(requestDto.getMemberEmail())
                .orElseThrow(() -> new NotFoundMemberException());

        MemberTeam newMemberTeam = MemberTeam.createMemberTeamEntity()
                .team(findTeam)
                .member(invitedMember)
                .teamMemberStatus(TeamMemberStatus.WAIT)
                .teamAuthority(TeamAuthority.ROLE_CHILD)
                .build();

        memberTeamRepository.save(newMemberTeam);
    }

    public List<InvitationListRequestDto> getAllInvitationToTeam(Long memberId) {
        return memberTeamRepository.findAllByMemberId(memberId, TeamMemberStatus.WAIT).stream()
                .map(InvitationListRequestDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyInviteTeamRequest(InviteTeamRequestDto requestDto, Long memberId, String teamName) {
        if (requestDto.getRequest().equals("ACCEPT")) {

            memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                    .orElseThrow(() -> new NotFoundMemberTeamException())
                    .updateTeamInvite(TeamMemberStatus.ACCEPT);

        } else if (requestDto.getRequest().equals("DENIED")) {

            // DB 상에서 삭제하지 않는 이유는 나중에 팀 관리 페이지 멤버 초대쪽에서 로그로 보여주기 위함.
            memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                    .orElseThrow(() -> new NotFoundMemberTeamException())
                    .updateTeamInvite(TeamMemberStatus.DENIED);

        } else {
            throw new RuntimeException();
        }


    }

    @Transactional
    public void setDevelopFieldToMember(SetDevelopFieldRequestDto requestDto, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMemberEmail(requestDto.getMemberEmail(), teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        if (requestDto.getIsDeleted()) {
            findMemberTeam.updateDevelopField(null);
            return;
        }

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

    public void memberAccessToTeam(Long memberId, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElse(null);

        if (findMemberTeam == null) {
            throw new NotAccessException();
        }
    }
}
