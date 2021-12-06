package capstone.gitime.api.controller;

import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final TodoService todoService;

    @GetMapping("/test")
    public ResultResponseDto<String> test() {
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());

    }

}
