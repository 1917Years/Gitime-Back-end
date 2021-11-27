package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.api.service.dto.TeamInfoResponseDto;
import capstone.gitime.domain.common.service.dto.GitRepoResponseDto;
import capstone.gitime.domain.memberTeam.service.MemberTeamService;
import capstone.gitime.domain.team.service.TeamService;
import capstone.gitime.domain.team.service.dto.CreateTeamRequestDto;
import capstone.gitime.domain.team.service.dto.TeamNoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final MemberTeamService memberTeamService;

    @GetMapping
    public Page<TeamInfoResponseDto> teamList(@RequestParam("page") int page, @Token Long memberId) {
        return memberTeamService.getMemberTeamList(memberId, page);
    }

    @GetMapping("/add")
    public ResultResponseDto<GitRepoResponseDto> gitRepoList(@Token Long memberId) {
        return new ResultResponseDto<>(200, "OK", teamService.getRepoList(memberId));
    }

    @PostMapping("/add")
    public ResultResponseDto<String> addTeam(@RequestBody CreateTeamRequestDto requestDto, @Token Long memberId) {
        teamService.createNewTeam(requestDto, memberId);
        return new ResultResponseDto<>(200, "OK", Collections.emptyList());
    }

    @GetMapping("/{teamName}/notice")
    public ResultResponseDto<TeamNoticeResponseDto> getNoticeLogList(@PathVariable("teamName") String teamName) {
        return new ResultResponseDto<>(200, "OK!", List.of(teamService.getTeamNotice(teamName)));
    }




}
