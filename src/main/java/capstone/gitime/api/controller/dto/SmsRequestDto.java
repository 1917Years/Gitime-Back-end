package capstone.gitime.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SmsRequestDto {

    private String code;
    private String num;
}
