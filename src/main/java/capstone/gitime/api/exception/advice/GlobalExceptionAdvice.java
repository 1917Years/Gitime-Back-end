package capstone.gitime.api.exception.advice;


import capstone.gitime.api.exception.dto.ErrorDetailDto;
import capstone.gitime.api.exception.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponseDto result = new ErrorResponseDto();
        e.getFieldErrors().forEach(error -> {
            result.getErrorDetail().add(new ErrorDetailDto(error.getField(), error.getDefaultMessage()));
        });
        result.setStatusCode(400);

        return result;
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto methodArgumentNotValidException(HttpMessageNotReadableException e) {
        return new ErrorResponseDto(400,e.getLocalizedMessage(),null);
    }
}
