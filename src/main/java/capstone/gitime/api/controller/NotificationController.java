package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.notification.service.NotificationService;
import capstone.gitime.domain.notification.service.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/list")
    public ResultResponseDto<NotificationResponseDto> getLatestNotificationMember(@Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", notificationService.getLatestNotificationByMember(memberId));
    }


    @GetMapping("/{teamName}/list/recent")
    public ResultResponseDto<NotificationResponseDto> getLatestNotificationTeam(@PathVariable("teamName") String teamName) {
        return new ResultResponseDto<>(200, "OK!", notificationService.getLatestNotificationByTeamName(teamName));
    }

    @GetMapping("/{teamName}/list/upcoming")
    public ResultResponseDto<NotificationResponseDto> getUpcomingNotificationTeam(@PathVariable("teamName") String teamName) {
        return new ResultResponseDto<>(200, "OK!", notificationService.getUpcomingNotificationByTeamName(teamName));
    }

}
