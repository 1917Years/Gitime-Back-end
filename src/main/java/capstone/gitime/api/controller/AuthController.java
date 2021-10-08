package capstone.gitime.api.controller;

import capstone.gitime.api.common.token.TokenDto;
import capstone.gitime.api.controller.dto.OwnMemberJoinDto;
import capstone.gitime.api.controller.dto.OwnMemberLoginDto;
import capstone.gitime.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String join(@RequestBody OwnMemberJoinDto joinDto) {
        authService.ownJoin(joinDto);
        return "OK!";
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody OwnMemberLoginDto loginDto) {
        return authService.ownLogin(loginDto);
    }


}
