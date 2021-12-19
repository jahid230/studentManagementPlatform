package com.learn.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {


    @NotNull(message="Student Id required")
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
}
