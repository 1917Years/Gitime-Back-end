package capstone.gitime.domain.member.service;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.common.token.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public interface OauthService<T> {

    TokenDto getToken(String code) throws JsonProcessingException;

    TokenDto postTokenToGetInfo(String token) throws JsonProcessingException;

    default HttpEntity createHttpEntity(String token, MediaType mediaType, MultiValueMap body){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(mediaType);

        if (StringUtils.hasText(token)) {
            httpHeaders.add("Authorization","Bearer " + token);
        }
        return new HttpEntity(body,httpHeaders);
    };

    T parseToDto(String body) throws JsonProcessingException;

}
