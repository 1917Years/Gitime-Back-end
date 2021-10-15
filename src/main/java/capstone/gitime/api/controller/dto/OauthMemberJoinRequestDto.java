package capstone.gitime.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OauthMemberJoinRequestDto {

    private String userName;
    private String nickName;
    private String phoneNumber;
    private String birth;

}
