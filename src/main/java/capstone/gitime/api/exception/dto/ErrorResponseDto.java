package capstone.gitime.api.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private int statusCode;
    private String globalErrorMessage;
    private List<ErrorDetailDto> errorDetail = new ArrayList<>();


}
