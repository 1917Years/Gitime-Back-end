package capstone.gitime.api.exception.advice;

import capstone.gitime.api.exception.dto.ErrorResponseDto;
import capstone.gitime.api.exception.exception.member.MemberJoinException;
import capstone.gitime.api.exception.exception.member.MemberLoginException;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class MemberExceptionAdvice {

    @ExceptionHandler(value = {MemberLoginException.class, BadCredentialsException.class, MemberJoinException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto memberFuncException(RuntimeException e) {
        if (e.getClass().isAssignableFrom(BadCredentialsException.class)) {
            return new ErrorResponseDto(400, "패스워드가 올바르지 않습니다.", null);
        }
        return new ErrorResponseDto(400, e.getMessage(), null);
    }

    @ExceptionHandler(value = {NotFoundMemberException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto notFoundMemberException(NotFoundException e) {
        return new ErrorResponseDto(400, e.getMessage(), null);
    }
}
