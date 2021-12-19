package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.NotificationRequestDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.notification.service.NotificationService;
import capstone.gitime.domain.notification.service.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/list")
    public ResultResponseDto<NotificationResponseDto> getAllMemberNotification(@Token Long memberId) {
        return new ResultResponseDto<>(200, "OK!", notificationService.getAllNotification(memberId));
    }

    @PostMapping("/add/{teamName}")
    public ResultResponseDto<String> alertNotification(@Token Long memberId,
                                                       @RequestBody NotificationRequestDto requestDto,
                                                       @PathVariable("teamName") String teamName) {
        notificationService.alertAllTeamMember(memberId, teamName, requestDto);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

}
