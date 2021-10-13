package capstone.gitime.domain.member.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleOauthTokenDto {

    private String access_token;
    private Long expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;
}
