package capstone.gitime.domain.todo.service.dto;

import capstone.gitime.domain.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TodoListResponseDto {

    private String todo;
    private String developField;
    private LocalDate startDate;
    private LocalDate untilDate;
    private Boolean isFinish;

    public static TodoListResponseDto of(Todo todo) {
        return new TodoListResponseDto(todo.getWorking(), todo.getDevelopField() == null ? null : todo.getDevelopField().getField(),
                LocalDate.of(todo.getCreatedAt().getYear(),
                        todo.getCreatedAt().getMonth(),
                        todo.getCreatedAt().getDayOfMonth()),
                todo.getUntilDate(),
                todo.getIsFinish());
    }

}
