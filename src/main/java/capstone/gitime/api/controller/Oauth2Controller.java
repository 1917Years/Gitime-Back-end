package capstone.gitime.api.controller;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.domain.member.service.OauthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class Oauth2Controller {

    @Qualifier("kakao")
    private final OauthService kakaoService;

    @Qualifier("google")
    private final OauthService googleService;

    @GetMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto loginKakao(@RequestParam("code") String code) throws JsonProcessingException {
        return kakaoService.getToken(code);
    }

//    @GetMapping("/kakao")
//    @ResponseStatus(HttpStatus.OK)
//    public TokenDto loginNaver(@RequestParam("code") String code) throws JsonProcessingException {
//        return kakaoService.getToken(code);
//    }
//
    @GetMapping("/google")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto loginGoogle(@RequestParam("code") String code) throws JsonProcessingException {
        return googleService.getToken(code);
    }

}
