package capstone.gitime.domain.team.service;


import capstone.gitime.api.controller.dto.DeleteDevelopFieldRequestDto;
import capstone.gitime.api.controller.dto.TeamNoticeRequestDto;
import capstone.gitime.api.exception.exception.global.NotFoundException;
import capstone.gitime.api.exception.exception.member.NotAccessToInformation;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.DuplicateTeamNameException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.common.repository.GitRepoRepository;
import capstone.gitime.domain.common.service.dto.GitRepoResponseDto;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.developfield.repository.DevelopFieldRepository;
import capstone.gitime.domain.endpoint.service.EndPointService;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.entity.TeamAuthority;
import capstone.gitime.domain.memberteam.entity.TeamMemberStatus;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.entity.TeamNotice;
import capstone.gitime.domain.team.entity.TeamStatus;
import capstone.gitime.domain.team.repository.TeamNoticeRepository;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.team.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final GitRepoRepository gitRepoRepository;
    private final DevelopFieldRepository developFieldRepository;
    private final TeamNoticeRepository teamNoticeRepository;
    private final EndPointService endPointService;

    public List<GitRepoResponseDto> getRepoList(Long memberId) {
        return gitRepoRepository.findListBy(memberId)
                .stream()
                .map(GitRepoResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createNewTeam(CreateTeamRequestDto requestDto, Long memberId) {

        if (teamRepository.findTeamByName(requestDto.getTeamName())
                .isPresent()) {
            throw new DuplicateTeamNameException("팀 명이 중복됩니다.");
        }

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("멤버를 조회할수 없습니다. PK 값 불량"));

        GitRepo findGitRepo = gitRepoRepository.findByUrl(requestDto.getGitRepoUrl(), memberId)
                .orElseThrow(() -> new RuntimeException());

        //채팅방 생성

        Team newTeam = Team.createTeamEntity()
                .teamName(requestDto.getTeamName())
                .teamDescription(requestDto.getTeamDescription())
                .gitRepo(findGitRepo)
                .developType(requestDto.getDevelopType())
                .teamStatus(TeamStatus.ACTIVE)
                .chatUUID("Asdasd")
                .build();

        teamRepository.save(newTeam);

        MemberTeam newMemberTeam = MemberTeam.createMemberTeamEntity()
                .team(newTeam)
                .member(findMember)
                .teamAuthority(TeamAuthority.ROLE_PARENT)
                .teamMemberStatus(TeamMemberStatus.ACCEPT)
                .build();

        memberTeamRepository.save(newMemberTeam);

        endPointService.codeFileDownloadAndSave(newTeam);



    }

    public TeamAdminInfoResponseDto getTeamInfo(Long memberId, String teamName) {

        MemberTeam findMemberTeam = memberTeamRepository.findFetchTeamByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        Integer totalMembers = teamRepository.countAllByTeamName(teamName);

        return TeamAdminInfoResponseDto.of(findMemberTeam,totalMembers);
    }

    @Transactional
    public void updateTeamInfo(UpdateTeamInfoRequestDto requestDto, Long memberId, String teamName) {

        memberAccessCheck(memberId, teamName);

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        findTeam.updateTeamInfo(requestDto);
    }

    @Transactional
    public void updateTeamNotice(TeamNoticeRequestDto requestDto, Long memberId, String teamName) {

        memberAccessCheck(memberId, teamName);

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        TeamNotice newTeamNotice = TeamNotice.createTeamNotice()
                .notice(requestDto.getNotice())
                .team(findTeam)
                .build();

        teamNoticeRepository.save(newTeamNotice);
    }

    public TeamNoticeResponseDto getTeamNotice(String teamName) {

        List<TeamNotice> teamNotices = teamRepository.findNoticeByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException())
                .getTeamNotices();

        if (teamNotices.isEmpty()) {
            return new TeamNoticeResponseDto("공지사항을 등록해주세요.", LocalDate.now());
        }

        TeamNotice result = teamNotices.get(0);

        for (int i = 1; i < teamNotices.size(); i++) {
            if (teamNotices.get(i).getCreatedAt().isAfter(result.getCreatedAt())) {
                result = teamNotices.get(i);
            }
        }

        return TeamNoticeResponseDto.of(result);
    }

    public List<TeamNoticeResponseDto> getAllTeamNotice(Long memberId, String teamName) {
        memberAccessCheck(memberId, teamName);

        List<TeamNoticeResponseDto> result = teamRepository.findNoticeByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException())
                .getTeamNotices()
                .stream().map(TeamNoticeResponseDto::of)
                .collect(Collectors.toList());

        Collections.reverse(result);

        return result;
    }

    public List<DevelopFieldResponseDto> getAllDevelopField(Long memberId, String teamName) {

        return teamRepository.findDevelopFieldByTeamName(teamName)
                .stream().map(DevelopFieldResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createNewDevelopField(String field, Long memberId, String teamName) {

        memberAccessCheck(memberId, teamName);

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new RuntimeException());

        DevelopField createField = DevelopField.createDevelopField()
                .field(field)
                .team(findTeam)
                .build();

        developFieldRepository.save(createField);
    }

    @Transactional
    public void removeDevelopField(DeleteDevelopFieldRequestDto requestDto, Long memberId, String teamName) {
        memberAccessCheck(memberId, teamName);

        DevelopField findDevelopField = developFieldRepository.findByField(requestDto.getDevelopField(), teamName)
                .orElseThrow(() -> new NotFoundException());

        developFieldRepository.delete(findDevelopField);
    }

    public List<TeamMemberListInfoRequestDto> getTeamMemberListInfo(Long memberId, String teamName) {

        return memberTeamRepository.findByAcceptAndTeamName(teamName)
                .stream()
                .map(TeamMemberListInfoRequestDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTeam(Long memberId, String teamName) {
        memberAccessCheck(memberId, teamName);

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        findTeam.updateTeamStatus(TeamStatus.DIE);

    }

    public MemberTeam memberAccessCheck(Long memberId, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        if (!findMemberTeam.getTeamAuthority().equals(TeamAuthority.ROLE_PARENT)) {
            throw new NotAccessToInformation("해당 API에 권한이 주어지지 않았습니다.");
        }

        return findMemberTeam;
    }
}
