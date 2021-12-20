package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.CreateTodoRequestDto;
import capstone.gitime.api.controller.dto.ModifyTodoRequestDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.endpoint.service.EndPointService;
import capstone.gitime.domain.endpoint.service.dto.EndPointResponseDto;
import capstone.gitime.domain.endpoint.service.dto.ShellCommandResponseDto;
import capstone.gitime.domain.team.service.TeamService;
import capstone.gitime.domain.todo.service.TodoService;
import capstone.gitime.domain.todo.service.dto.TodoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final TeamService teamService;
    private final TodoService todoService;
    private final EndPointService endPointService;

    @GetMapping("/{teamName}/progress")
    public ResultResponseDto<Map<String, Double>> getTeamProgress(@Token Long memberId,
                                                                  @PathVariable("teamName") String teamName) {
        return new ResultResponseDto<>(200, "OK!", List.of(todoService.getDevelopProgress(teamName)));
    }

    @GetMapping("/{teamName}/todo")
    public ResultResponseDto<TodoListResponseDto> getTodoList(@Token Long memberId,
                                                              @PathVariable("teamName") String teamName) {
        return new ResultResponseDto<>(200, "OK!", todoService.getAllTodo(teamName));
    }

    @PostMapping("{teamName}/todo/modify")
    public ResultResponseDto<String> modifyTodoFinish(@Token Long memberId,
                                                      @PathVariable("teamName") String teamName,
                                                      @RequestBody ModifyTodoRequestDto requestDto) {
        todoService.modifyFinishTodo(requestDto, teamName, memberId);

        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @PostMapping("/{teamName}/todo/add")
    public ResultResponseDto<String> addTodo(@RequestBody CreateTodoRequestDto requestDto,
                                             @Token Long memberId,
                                             @PathVariable("teamName") String teamName) {
        todoService.createTodo(requestDto, memberId, teamName);

        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/endpoint")
    public ResultResponseDto<EndPointResponseDto> getEndPoint(@PathVariable("teamName") String teamName) {

        return new ResultResponseDto<>(200, "OK!", List.of(endPointService.getEndPointInfo(teamName)));
    }

    @PostMapping("/{teamName}/endpoint/create")
    public ResultResponseDto<ShellCommandResponseDto> createEndPoint(@RequestParam("dockerFile") MultipartFile multipartFile,
                                                                     @PathVariable("teamName") String teamName) throws IOException, InterruptedException {

        endPointService.createDocker(multipartFile, "8081", teamName);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }
    @GetMapping("/{teamName}/endpoint/stop")
    public ResultResponseDto<ShellCommandResponseDto> stopEndPoint(@PathVariable("teamName") String teamName) throws IOException, InterruptedException {

        endPointService.stopDocker(teamName);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @GetMapping("/{teamName}/endpoint/run")
    public ResultResponseDto<ShellCommandResponseDto> startEndPoint(@PathVariable("teamName") String teamName) throws IOException, InterruptedException {

        endPointService.runDocker(teamName);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

}
