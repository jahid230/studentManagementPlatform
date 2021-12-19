package com.learn.controller;

import com.learn.entity.Student;
import com.learn.request.CreateStudentRequest;
import com.learn.request.InQueryRequest;
import com.learn.request.UpdateStudentRequest;
import com.learn.response.StudentResponse;
import com.learn.service.StudentService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.learn.response.StudentResponse;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/student/")

public class StudentContoller {

    // Error<Warn<Info<Debug<Trace



    @Autowired
    StudentService studentService;

    @Value("${app.name:Learning App}")
    private String appName;


    @GetMapping("/getAll")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @PostMapping("create")
    public StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){
       Student student= studentService.createStudent(createStudentRequest);
       return new StudentResponse(student);
    }

    @PutMapping("update")
    public StudentResponse updateStudent(@Valid @RequestBody UpdateStudentRequest updateStudentRequest){
        Student student=studentService.updateStudent(updateStudentRequest);
        return new StudentResponse(student);
    }

    @DeleteMapping("delete/{id}")
    public String deleteStudent(@PathVariable Long id){
       return  studentService.deleteStudent(id);
    }

    @GetMapping("getfirstNameBYId/{firstName}")
    public List<StudentResponse> getFirstNameById(@PathVariable String firstName){

        List<Student> students=studentService.getStudentByFirstName(firstName);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }
    @GetMapping("getByEmail/{email}")
    public List<StudentResponse> getByEmail(@PathVariable String email){
        List<Student> students=studentService.getStudentByEmail(email);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;

    }

    @GetMapping("getByFirstNameAndLastName/{firstName}/{lastName}")
    public StudentResponse getByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName){

        Student student= studentService.getStudentByFirstNameAndLastName(firstName,lastName);
        return new StudentResponse(student);

    }
    @GetMapping("getByFirstNameIn")
    public List<StudentResponse> getByFirstNameIn(@RequestBody InQueryRequest inQueryRequest){
        List<Student> students=studentService.getInQueryFirstNames(inQueryRequest);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }

    @GetMapping("getStudentPagination/{pageNo}/{pageSize}")
    public List<StudentResponse> getStudentPagination(@PathVariable int pageNo,@PathVariable int pageSize){

        List<Student> students= studentService.getStudentPagination(pageNo,pageSize);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }

    @GetMapping("getAllWithSorting")
    public List<StudentResponse> getAllWithSorting(){

        List<Student> students= studentService.getStudentsWithSorting();
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }

    @GetMapping("like/{firstName}")
    public List<StudentResponse> like(@PathVariable String firstName){

        List<Student> students= studentService.like(firstName);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }


    @GetMapping("startsWith/{firstName}")
    public List<StudentResponse> startsWith(@PathVariable String firstName){

        List<Student> students= studentService.startsWith(firstName);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }

    @PutMapping("updateFirstName/{id}/{firstName}")
    public String updateStudentWithJpql(@PathVariable Long id, @PathVariable String firstName){
        return studentService.updateStudentWithJpql(id, firstName)+ "Student Updated";
    }



    @GetMapping("getByAddressCity/{city}")
    public List<StudentResponse> getByAddressCity(@PathVariable String city){

        List<Student> students= studentService.getByAddressCity(city);
        List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
        students.stream().forEach(student -> {
            studentResponses.add(new StudentResponse(student));
        });
        return studentResponses;
    }


}
