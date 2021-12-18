package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.*;
import capstone.gitime.domain.memberteam.service.MemberTeamService;
import capstone.gitime.domain.memberteam.service.dto.InviteMemberListRequestDto;
import capstone.gitime.domain.team.service.TeamService;
import capstone.gitime.domain.team.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/admin")
@Slf4j
public class TeamAdminController {

    private final TeamService teamService;
    private final MemberTeamService memberTeamService;

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

    @PostMapping("/{teamName}/developfield/member")
    public ResultResponseDto<String> setDevelopFieldToTeamMember(@PathVariable("teamName") String teamName,
                                                                 @RequestBody SetDevelopFieldRequestDto requestDto) {

        log.info("{}",requestDto.getIsDeleted());
        memberTeamService.setDevelopFieldToMember(requestDto, teamName);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/members")
    public ResultResponseDto<TeamMemberListInfoRequestDto> getTeamMemberList(@PathVariable("teamName") String teamName,
                                                                             @Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", teamService.getTeamMemberListInfo(memberId, teamName));
    }

    @GetMapping("/{teamName}/members/invite")
    public ResultResponseDto<Page<InviteMemberListRequestDto>> getInviteTeamMemberList(@PathVariable("teamName") String teamName,
                                                                                       @RequestParam(value = "page", defaultValue = "0") String page) {
        return new ResultResponseDto<>(200, "OK!", List.of(memberTeamService.getMemberTeamInviteList(teamName, Integer.valueOf(page))));
    }

}
