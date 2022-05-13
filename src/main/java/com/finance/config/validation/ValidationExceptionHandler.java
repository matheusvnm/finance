package com.finance.config.validation;

import com.finance.dto.ErroDeValidacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeValidacaoDTO> handle(MethodArgumentNotValidException exception){
        List <ErroDeValidacaoDTO> validationErrors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getFieldErrors();
        fieldErrors.parallelStream().forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeValidacaoDTO erroDeValidacaoDTO = new ErroDeValidacaoDTO(e.getField(), mensagem);
            validationErrors.add(erroDeValidacaoDTO);
        });
        return validationErrors;
    }


}
