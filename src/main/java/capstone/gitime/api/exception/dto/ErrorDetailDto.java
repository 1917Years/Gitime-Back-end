package capstone.gitime.api.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailDto {
    private String filedName;
    private String errorDetail;
}
