package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.controller.dto.OauthMemberJoinRequestDto;
import capstone.gitime.api.controller.dto.OwnMemberJoinRequestDto;
import capstone.gitime.api.controller.dto.OwnMemberLoginRequestDto;
import capstone.gitime.api.controller.dto.SmsRequestDto;
import capstone.gitime.domain.member.service.AuthService;
import capstone.gitime.domain.member.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final SmsService joinService;

    @PostMapping("/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String join(@RequestBody @Validated OwnMemberJoinRequestDto joinDto) {
        authService.ownJoin(joinDto);
        return "OK!";
    }

    @PostMapping("/join/oauth")
    public String joinOauth(@RequestBody @Validated OauthMemberJoinRequestDto joinDto, @Token Long memberId) {
        authService.oauthJoin(joinDto,memberId);
        return "OK!";
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody @Validated OwnMemberLoginRequestDto loginDto) {
        return authService.ownLogin(loginDto);
    }

    @PostMapping("/sms")
    public String authSms(@RequestBody SmsRequestDto requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        joinService.smsSend(requestDto);
        return "OK!";
    }
}
