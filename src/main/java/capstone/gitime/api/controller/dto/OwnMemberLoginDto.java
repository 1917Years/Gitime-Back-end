package capstone.gitime.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OwnMemberLoginDto {
    private String email;
    private String password;
}
