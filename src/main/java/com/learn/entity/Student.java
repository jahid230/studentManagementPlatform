package com.learn.entity;

import com.learn.request.CreateStudentRequest;
import com.learn.response.StudentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="student")
public class Student {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name="id")
    private UUID id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email",unique = true)
    private String email;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "student")
    private List<Subject> learningSubjects;

    @Transient
    private String fullName;

    public Student(CreateStudentRequest createStudentRequest){

        this.firstName= createStudentRequest.getFirstName();
        this.lastName=createStudentRequest.getLastName();
        this.email=createStudentRequest.getEmail();
        this.fullName=createStudentRequest.getLastName()+" "+createStudentRequest.getLastName();

    }
}
