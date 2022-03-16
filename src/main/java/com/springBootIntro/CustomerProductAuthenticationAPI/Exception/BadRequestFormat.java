package com.springBootIntro.CustomerProductAuthenticationAPI.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestFormat extends RuntimeException{
    public BadRequestFormat(String message){
        super(message);
    }

}
