package capstone.gitime.api.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTodoRequestDto {

    private String field;
    private String working;
    private LocalDate untilDate;

}
