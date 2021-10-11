package capstone.gitime.api.exception.advice;

import capstone.gitime.api.exception.dto.ErrorResponseDto;
import capstone.gitime.api.exception.exception.member.MemberLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class MemberExceptionAdvice {

    @ExceptionHandler(value = {MemberLoginException.class, BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto notFoundMemberException(RuntimeException e) {
        if (e.getClass().isAssignableFrom(BadCredentialsException.class)) {
            return new ErrorResponseDto(400, "패스워드가 올바르지 않습니다.", null);
        }
        return new ErrorResponseDto(400, e.getMessage(), null);
    }

}
