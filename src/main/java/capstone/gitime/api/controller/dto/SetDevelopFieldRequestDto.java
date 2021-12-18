package capstone.gitime.api.controller.dto;

import lombok.Data;

@Data
public class SetDevelopFieldRequestDto {

    private Boolean isDeleted;
    private String memberEmail;
    private String developField;

}
