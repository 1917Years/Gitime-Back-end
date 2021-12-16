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
    private LocalDate createdDate;
    private Boolean isFinish;

    public static TodoListResponseDto of(Todo todo) {
        LocalDateTime date = todo.getCreatedAt();

        return new TodoListResponseDto(todo.getWorking(), todo.getDevelopField().getField(), LocalDate.of(date.getYear(),
                date.getMonth(),
                date.getDayOfMonth()),
                todo.getIsFinish());
    }

}
