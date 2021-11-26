package capstone.gitime.domain.team.service;


import capstone.gitime.api.controller.dto.TeamNoticeRequestDto;
import capstone.gitime.api.exception.exception.member.NotAccessToInformation;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.api.service.dto.TeamInfoResponseDto;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.common.repository.GitRepoRepository;
import capstone.gitime.domain.common.service.dto.GitRepoResponseDto;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.developfield.repository.DevelopFieldRepository;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.memberTeam.entity.MemberTeam;
import capstone.gitime.domain.memberTeam.entity.TeamAuthority;
import capstone.gitime.domain.memberTeam.entity.TeamInvite;
import capstone.gitime.domain.memberTeam.repository.MemberTeamRepository;
import capstone.gitime.domain.team.entity.DevelopType;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.entity.TeamNotice;
import capstone.gitime.domain.team.repository.TeamNoticeRepository;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.team.service.dto.CreateTeamRequestDto;
import capstone.gitime.domain.team.service.dto.DevelopFieldResponseDto;
import capstone.gitime.domain.team.service.dto.TeamNoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<GitRepoResponseDto> getRepoList(Long memberId) {
        return gitRepoRepository.findListBy(memberId)
                .stream()
                .map(GitRepoResponseDto::of)
                .collect(Collectors.toList());
    }

    public void createNewTeam(CreateTeamRequestDto requestDto, Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("멤버를 조회할수 없습니다. PK 값 불량"));

        GitRepo findGitRepo = gitRepoRepository.findByUrl(requestDto.getGitRepoUrl())
                .orElseThrow(() -> new RuntimeException());

        Team newTeam = Team.createTeamEntity()
                .teamName(requestDto.getTeamName())
                .teamDescription(requestDto.getTeamDescription())
                .gitRepo(findGitRepo)
                .developType(requestDto.getDevelopType())
                .build();

        teamRepository.save(newTeam);

        MemberTeam newMemberTeam = MemberTeam.createMemberTeamEntity()
                .team(newTeam)
                .member(findMember)
                .teamAuthority(TeamAuthority.ROLE_PARENT)
                .teamInvite(TeamInvite.ACCEPT)
                .build();

        memberTeamRepository.save(newMemberTeam);
    }

    @Transactional
    public void updateTeamNotice(TeamNoticeRequestDto requestDto, Long memberId, String teamName) {

        if (memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException())
                .getTeamAuthority().equals(TeamAuthority.ROLE_PARENT)) {

            Team findTeam = teamRepository.findTeamByName(teamName)
                    .orElseThrow(() -> new NotFoundTeamException());

            TeamNotice newTeamNotice = TeamNotice.createTeamNotice()
                    .notice(requestDto.getNotice())
                    .team(findTeam)
                    .build();

            teamNoticeRepository.save(newTeamNotice);

        } else {
            throw new NotAccessToInformation("팀에 대한 해당 권한이 주어지지 않았습니다.");
        }

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

    public List<TeamNoticeResponseDto> getAllTeamNotice(String teamName) {
        return teamRepository.findNoticeByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException())
                .getTeamNotices()
                .subList(0,5)
                .stream().map(TeamNoticeResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<DevelopFieldResponseDto> getAllDevelopField(Long teamId) {
        return teamRepository.findAllById(teamId)
                .stream().map(DevelopFieldResponseDto::of)
                .collect(Collectors.toList());
    }

    public void createNewDevelopField(String field, Long teamId) {
        Team findTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException());

        DevelopField createField = DevelopField.createDevelopField()
                .field(field)
                .team(findTeam)
                .build();

        developFieldRepository.save(createField);
    }
}
