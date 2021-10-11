package capstone.gitime.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class OwnMemberLoginRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
