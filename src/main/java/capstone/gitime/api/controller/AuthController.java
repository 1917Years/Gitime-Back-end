package capstone.gitime.api.controller;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.controller.dto.OwnMemberJoinRequestDto;
import capstone.gitime.api.controller.dto.OwnMemberLoginRequestDto;
import capstone.gitime.api.controller.dto.SmsRequestDto;
import capstone.gitime.domain.member.service.AuthService;
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
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String join(@RequestBody @Validated OwnMemberJoinRequestDto joinDto) {
        authService.ownJoin(joinDto);
        return "OK!";
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody @Validated OwnMemberLoginRequestDto loginDto) {
        return authService.ownLogin(loginDto);
    }

    @PostMapping("/sms")
    public String authSms(@RequestBody SmsRequestDto requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        authService.smsSend(requestDto);
        return "OK!";
    }
}
