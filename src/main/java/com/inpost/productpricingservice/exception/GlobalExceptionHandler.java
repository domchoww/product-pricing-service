package com.inpost.productpricingservice.exception;

import com.inpost.productpricingservice.model.ErrorResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({ProductsNotFoundException.class})
    @ResponseBody
    protected ErrorResponseDto handleCustomException(ProductsNotFoundException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }
}
