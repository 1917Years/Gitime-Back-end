package capstone.gitime.api.controller.dto;

import lombok.Data;

@Data
public class CreateBoardRequestDto {
    private String title;
    private String content;

    // TODO: 2021/12/16 Adding Upload File

}
