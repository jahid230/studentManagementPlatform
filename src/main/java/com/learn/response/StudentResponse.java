package com.learn.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learn.entity.Student;
import com.learn.entity.Subject;
import com.learn.service.StudentService;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudentResponse {
    @JsonIgnore
    private long id;

    @JsonProperty("first_name")
    private String firstName;
    private String lastName;
    private String email;
    private String fullName;


    private String city;
    private String street;

    private List<SubjectResponse> subjectResponses;

    public StudentResponse(Student student) {
        this.firstName=student.getFirstName();
        this.lastName=student.getLastName();
        this.email=student.getEmail();
        this.fullName=student.getFirstName()+" "+student.getLastName();
        this.city=student.getAddress().getCity();
        this.street=student.getAddress().getStreet();

        if(student.getLearningSubjects() !=null){
            subjectResponses=new ArrayList<SubjectResponse>();
            for (Subject subject: student.getLearningSubjects()){
                subjectResponses.add(new SubjectResponse(subject));
            }

        }


    }
}
