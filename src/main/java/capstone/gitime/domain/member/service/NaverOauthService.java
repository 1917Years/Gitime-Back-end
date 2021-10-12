package capstone.gitime.domain.member.service;

import capstone.gitime.api.common.token.TokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public class NaverOauthService implements OauthService{
    @Override
    public TokenDto getToken(String code) throws JsonProcessingException {
        return null;
    }

    @Override
    public TokenDto postTokenToGetInfo(String token) throws JsonProcessingException {
        return null;
    }

    @Override
    public Object parseToDto(String body) throws JsonProcessingException {
        return null;
    }
}
