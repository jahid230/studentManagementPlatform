package com.springBootIntro.CustomerProductAuthenticationAPI.Exception;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExceptionResponse extends RuntimeException{


    private Date timestamp;
    private String message;
    private String Details;

}
