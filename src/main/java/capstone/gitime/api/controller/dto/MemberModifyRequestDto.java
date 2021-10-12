package capstone.gitime.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberModifyRequestDto {

    private String password;
    private String nickName;
    private String phoneNumber;

}