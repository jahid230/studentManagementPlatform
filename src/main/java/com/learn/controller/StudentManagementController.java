package com.learn.controller;


import com.learn.entity.Address;
import com.learn.entity.Student;
import com.learn.entity.Subject;
import com.learn.request.CreateStudentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.List;

@RestController
@RequestMapping("/api/student/")
public class StudentManagementController {

    @GetMapping("/getAll")
    public List<Student> getAllStudents(){
        return studentServiceMngService.getAllStudents();
    }

}

@Service
public class StudentService {


    public List<Student> getAllStudents(){
        //logger.info(studentRepository.findAll().get(0).getFullName());
        return studentRepository.findAll();
    }
}


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email",unique = true)
    private String email;


    @OneToMany(mappedBy = "student")
    private List<Subject> learningSubjects;


    public Student(CreateStudentRequest createStudentRequest){

        this.firstName= createStudentRequest.getFirstName();
        this.lastName=createStudentRequest.getLastName();
        this.email=createStudentRequest.getEmail();

    }
}


@Repository
public interface StudentRepository extends JpaRepository<com.learn.entity.Student,Long> {


}






//2.
//There will be a model name Subbject model like this

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="subject_name")
    private String subjectName;

    @Column(name="marks_obtained")
    private Double marksObtained;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
//in the student entity we ndefine the relationship as like this

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email",unique = true)
    private String email;


    @OneToMany(mappedBy = "student")
    private List<Subject> learningSubjects;


    public Student(CreateStudentRequest createStudentRequest){

        this.firstName= createStudentRequest.getFirstName();
        this.lastName=createStudentRequest.getLastName();
        this.email=createStudentRequest.getEmail();

    }
}


3. It will print  true then new line and print true and then new line and print false

4. for this we need a function

public List<Integer> removeDuplicate(ArrayList<Integer> list){

    ArrayList<Integer> newList= new ArrayList<Integer>()';
        for (Integer singleint: list){

            if(!newList.contains(singleint)){
                newList.add(singleint);
        }
        }

        return newList;
        }


        





        5. update student.active set value(active=true) from students where student_id in (2,5,6)