package com.learn.service;


import com.learn.entity.Address;
import com.learn.entity.Student;
import com.learn.entity.Subject;
import com.learn.repository.AddressRepository;
import com.learn.repository.SubjectRepository;
import com.learn.request.CreateStudentRequest;
import com.learn.request.CreateSubjectRequest;
import com.learn.request.InQueryRequest;
import com.learn.request.UpdateStudentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.learn.repository.StudentRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    SubjectRepository subjectRepository;
    public List<Student> getAllStudents(){
        logger.info(studentRepository.findAll().get(0).getFullName());
        return studentRepository.findAll();
    }

    public Student createStudent(CreateStudentRequest createStudentRequest){
        Student student= new Student(createStudentRequest);
        Address address= new Address();
        address.setCity(createStudentRequest.getCity());
        address.setStreet(createStudentRequest.getStreet());
        address=addressRepository.save(address);
        student.setAddress(address);
        student=studentRepository.save(student);
        List<Subject> subjectList=new ArrayList<Subject>();
        if(createStudentRequest.getLearningSubjects()!= null){
            for(CreateSubjectRequest createSubjectRequest: createStudentRequest.getLearningSubjects()){
                Subject subject=new Subject();
                subject.setSubjectName(createSubjectRequest.getSubjectName());
                subject.setMarksObtained(createSubjectRequest.getMarksObtained());
                subject.setStudent(student);
                subjectList.add(subject);
            }
          List<Subject> saved_subjects = subjectRepository.saveAll(subjectList);
            logger.info(saved_subjects.toString());
        }
        student.setLearningSubjects(subjectList);
        return student;
    }

    public Student updateStudent(UpdateStudentRequest updateStudentRequest){

        Student student=studentRepository.findById(updateStudentRequest.getId()).get();

        if(updateStudentRequest.getFirst_name() !=null && !updateStudentRequest.getFirst_name().isEmpty()){
            student.setFirstName(updateStudentRequest.getFirst_name());
        }
        student= studentRepository.save(student);
        return student;
    }

    public String deleteStudent(UUID id){
        studentRepository.deleteById(id);
        return "Deleted the Student";
    }

    public List<Student> getStudentByFirstName(String firstName){
        List<Student>  students= studentRepository.findByFirstName(firstName);
        return students;
    }
    public List<Student> getStudentByEmail(String email){
        List<Student>  students= studentRepository.findByEmail(email);
        return students;
    }

    public Student getStudentByFirstNameAndLastName(String firstName, String lastName){
        Student  student= studentRepository.findByFirstNameAndLastName(firstName,lastName);
        return student;
    }



    public List<Student> getInQueryFirstNames(InQueryRequest inQueryRequest){
        return studentRepository.findByFirstNameIn(inQueryRequest.getFirstnames());
    }

    public List<Student> getStudentPagination(int pageNo,int pageSize){
        Pageable pageable= PageRequest.of(pageNo-1,pageSize);
        return studentRepository.findAll(pageable).getContent();
    }

    public List<Student> getStudentsWithSorting(){
        Sort sort=Sort.by(Sort.Direction.ASC, "firstName");
        return studentRepository.findAll(sort);
    }

    public List<Student> like(String firstName){

        List<Student> students=studentRepository.findByFirstNameContains(firstName);
        return students;

    }

    public List<Student> startsWith(String firstName){

        List<Student> students=studentRepository.findByFirstNameStartsWith(firstName);
        return students;

    }
    public Integer updateStudentWithJpql(@PathVariable UUID id, @PathVariable String firstName){
        return studentRepository.updateFirstName(id, firstName);
    }

    public List<Student> getByAddressCity(String city){

        List<Student> students= studentRepository.getByAddressCity(city);
        return students;

    }


}
