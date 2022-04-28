package capstone.gitime.api.controller;

import capstone.gitime.api.common.ApiUtils;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.board.service.dto.BoardDetailResponseDto;
import capstone.gitime.domain.endpoint.service.dto.EndPointResponseDto;
import capstone.gitime.domain.team.service.dto.InviteTeamRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class GlobalController {


    @GetMapping(path = "{id}")
    public ApiUtils.ApiResult<BoardDetailResponseDto> findById(@PathVariable(name = "id") Long id) {
        return ApiUtils.success(new BoardDetailResponseDto("abc", "abc", "abc", "abc", LocalDateTime.now(),
                1, Collections.emptyList()));
    }

}
