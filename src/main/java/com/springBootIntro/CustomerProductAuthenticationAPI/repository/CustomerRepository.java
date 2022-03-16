package com.springBootIntro.CustomerProductAuthenticationAPI.repository;

import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {


//    List<Student> findByFirstName(String firstName);
//    List<Student> findByEmail(String email);
//    Student findByFirstNameAndLastName(String firstName,String lastName);
//    List<Student> findByFirstNameIn(List<String> firstnames);
//    List<Student> findByFirstNameContains(String firstName);
//    List<Student> findByFirstNameStartsWith(String firstName);
//
//    @Query("From Student where firstName= :firstName and lastName= :lastName")
//    Student getByFirstNameAndLastName(String firstName,String lastName);
//
//    @Modifying
//    @Transactional
//    @Query("Update Student set firstName= :firstName where id= :id")
//    Integer updateFirstName(Long id,String firstName);
//
//
//
//    @Query("From Student where address.city= :city")
//    List<Student> getByAddressCity(String city);




}
