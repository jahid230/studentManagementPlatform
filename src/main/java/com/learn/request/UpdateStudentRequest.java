package com.learn.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {


    @NotNull(message="Student Id required")
    private UUID id;
    private String first_name;
    private String last_name;
    private String email;
}
