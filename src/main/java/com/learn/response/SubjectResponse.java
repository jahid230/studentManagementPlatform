package com.learn.response;

import com.learn.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {


    private UUID id;

    private String subjectName;

    private Double marksObtained;

    public SubjectResponse(Subject subject){
        this.id=subject.getId();
        this.subjectName=subject.getSubjectName();
        this.marksObtained= subject.getMarksObtained();
    }

}
