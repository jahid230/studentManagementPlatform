package com.learn.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learn.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    @JsonProperty(value = "first_name")
    @NotBlank(message = "First Name required")
    private String firstName;

    private String lastName;

    private String email;

    private String city;
    private String street;

    private List<CreateSubjectRequest> learningSubjects;
}
