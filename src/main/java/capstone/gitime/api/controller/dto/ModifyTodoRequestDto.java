package capstone.gitime.api.controller.dto;

import lombok.Data;

@Data
public class ModifyTodoRequestDto {

    private Long todoId;
    private String working;
    private String fieldName;
    private Boolean isFinish;

}
