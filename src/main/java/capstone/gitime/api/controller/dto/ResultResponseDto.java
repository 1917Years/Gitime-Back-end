package capstone.gitime.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ResultResponseDto<T> {
    private int statusCode;
    private String statusMessage;
    private List<T> data = new ArrayList<>();

}
