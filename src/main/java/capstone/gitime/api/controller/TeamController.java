package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.api.service.dto.TeamInfoResponseDto;
import capstone.gitime.domain.common.service.dto.GitRepoResponseDto;
import capstone.gitime.domain.team.service.TeamService;
import capstone.gitime.domain.team.service.dto.CreateTeamRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public Page<TeamInfoResponseDto> teamList(@RequestParam("page") int page, @Token Long memberId) {
        return teamService.getMemberTeamList(memberId, page);
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
}
