package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.CreateTodoRequestDto;
import capstone.gitime.api.controller.dto.ModifyTodoRequestDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.team.service.TeamService;
import capstone.gitime.domain.todo.service.TodoService;
import capstone.gitime.domain.todo.service.dto.TodoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final TeamService teamService;
    private final TodoService todoService;

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


}
