package capstone.gitime.api.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubTokenRequestDto {

    private String access_token;
    private String scope;
    private String token_type;

}
