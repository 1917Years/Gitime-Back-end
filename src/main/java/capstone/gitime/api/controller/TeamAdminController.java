package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.AddDevelopFieldRequestDto;
import capstone.gitime.api.controller.dto.DeleteDevelopFieldRequestDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.api.controller.dto.TeamNoticeRequestDto;
import capstone.gitime.domain.team.service.TeamService;
import capstone.gitime.domain.team.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/admin")
public class TeamAdminController {

    private final TeamService teamService;

    @DeleteMapping("/{teamName}/team/delete")
    public ResultResponseDto<String> deleteTeam(@PathVariable("teamName") String teamName,
                                                @Token Long memberId) {
        teamService.deleteTeam(memberId, teamName);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/notice")
    public ResultResponseDto<TeamNoticeResponseDto> getNoticeLogList(@PathVariable("teamName") String teamName,
                                                                     @Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", teamService.getAllTeamNotice(memberId, teamName));
    }

    @PostMapping("/{teamName}/notice")
    public ResultResponseDto<String> postNotice(@PathVariable("teamName") String teamName,
                                                @Token Long memberId,
                                                @RequestBody TeamNoticeRequestDto teamNoticeRequestDto) {

        teamService.updateTeamNotice(teamNoticeRequestDto, memberId, teamName);

        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/info")
    public ResultResponseDto<TeamAdminInfoResponseDto> getTeamInfo(@PathVariable("teamName") String teamName,
                                                                   @Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", List.of(teamService.getTeamInfo(memberId, teamName)));
    }

    @PostMapping("/{teamName}/info")
    public ResultResponseDto<String> postTeamInfo(@PathVariable("teamName") String teamName,
                                                  @Token Long memberId,
                                                  @RequestBody UpdateTeamInfoRequestDto updateTeamInfoRequestDto) {
        teamService.updateTeamInfo(updateTeamInfoRequestDto, memberId, teamName);

        return new ResultResponseDto<>(202, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/developfield")
    public ResultResponseDto<DevelopFieldResponseDto> getDevelopField(@PathVariable("teamName") String teamName,
                                                                      @Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", teamService.getAllDevelopField(memberId, teamName));
    }

    @PostMapping("/{teamName}/developfield/add")
    public ResultResponseDto<String> addDevelopField(@PathVariable("teamName") String teamName,
                                                     @Token Long memberId,
                                                     @RequestBody AddDevelopFieldRequestDto requestDto) {
        teamService.createNewDevelopField(requestDto.getDevelopField(), memberId, teamName);

        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @PostMapping("/{teamName}/developfield/delete")
    public ResultResponseDto<String> deleteDevelopField(@PathVariable("teamName") String teamName,
                                                        @Token Long memberId,
                                                        @RequestBody DeleteDevelopFieldRequestDto requestDto) {
        teamService.removeDevelopField(requestDto, memberId, teamName);

        return new ResultResponseDto<>(202, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/members")
    public ResultResponseDto<TeamMemberListInfoRequestDto> getTeamMemberList(@PathVariable("teamName") String teamName,
                                                                             @Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", teamService.getTeamMemberListInfo(memberId, teamName));
    }

}
