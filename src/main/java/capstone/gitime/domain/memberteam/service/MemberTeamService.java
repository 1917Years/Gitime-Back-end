package capstone.gitime.domain.memberteam.service;

import capstone.gitime.api.controller.dto.CreateInviteRequestDto;
import capstone.gitime.api.controller.dto.SetDevelopFieldRequestDto;
import capstone.gitime.api.exception.exception.global.NotAccessException;
import capstone.gitime.api.exception.exception.global.NotFoundException;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.exception.exception.memberteam.DuplicateMemberTeamException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundAcceptCodeException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.api.service.MailService;
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
import capstone.gitime.domain.memberteam.service.dto.TeamAllListAdditionalResponseDto;
import capstone.gitime.domain.memberteam.service.dto.TeamAllListResponseDto;
import capstone.gitime.domain.notification.entity.NotificationImportance;
import capstone.gitime.domain.notification.entity.NotificationType;
import capstone.gitime.domain.notification.service.NotificationService;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.todo.entity.Todo;
import capstone.gitime.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private final NotificationService notificationService;
    private final MailService mailService;
    private final TodoRepository todoRepository;


    public TeamAllListResponseDto getMemberTeamList(Long memberId, int page) {

        Page<TeamInfoResponseDto> pages = memberTeamRepository.findLazyListPageByIdAndAccept(memberId, PageRequest.of(page, 5))
                .map(TeamInfoResponseDto::new);

        ArrayList<String> teamNameList = new ArrayList<>();

        Map<String, TeamAllListAdditionalResponseDto> additionalResponseDtoMap = new ConcurrentHashMap<>();

        pages.getContent().forEach((item) -> {
            teamNameList.add(item.getTeamName());
        });

        for (String teamName : teamNameList) {
            Integer finishCount = 0;
            List<Todo> todoList = todoRepository.findAllByTeam(teamName);
            for (int i = 0; i < todoList.size(); i++) {
                if (todoList.get(i).getIsFinish() == true) {
                    finishCount++;
                }
            }
            additionalResponseDtoMap.put(teamName, new TeamAllListAdditionalResponseDto(teamRepository.findTeamMemberCountByTeamName(teamName), List.of(finishCount, todoList.size()), (finishCount / Double.valueOf(todoList.size()) * 100)));
        }

        return new TeamAllListResponseDto(pages, additionalResponseDtoMap);
    }

    public Page<InviteMemberListRequestDto> getMemberTeamInviteList(String teamName, Integer page) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.Direction.DESC, "createdAt");

        return memberTeamRepository.findLazyListPageByTeamName(teamName, pageRequest)
                .map(InviteMemberListRequestDto::of);
    }

    @Transactional
    public void inviteMemberToTeam(String teamName, CreateInviteRequestDto requestDto) {

        if (memberTeamRepository.findByTeamNameAndMemberEmail(requestDto.getMemberEmail(), teamName).isPresent()) {
            throw new DuplicateMemberTeamException();
        }


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

        newMemberTeam.updateAcceptCode(UUID.randomUUID().toString());

        mailService.sendTeamInvitation(invitedMember.getEmail(), teamName, newMemberTeam.getAcceptCode());

        memberTeamRepository.save(newMemberTeam);
    }

    public List<InvitationListRequestDto> getAllInvitationToTeam(Long memberId) {
        return memberTeamRepository.findAllByMemberId(memberId).stream()
                .map(InvitationListRequestDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyInviteTeamRequest(String acceptCode, Long memberId) {

        MemberTeam findMemberTeam = memberTeamRepository.findByMemberAndAcceptCode(memberId, acceptCode)
                .orElseThrow(() -> new NotFoundAcceptCodeException());

        findMemberTeam.updateTeamInvite(TeamMemberStatus.ACCEPT);
        findMemberTeam.deleteAcceptCode();

        notificationService.pushNotificationToTeam(findMemberTeam, NotificationType.MEMBER, NotificationImportance.NORMAL,
                LocalDate.of(2030, 12, 10));

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
