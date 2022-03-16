package com.springBootIntro.CustomerProductAuthenticationAPI.service;


import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ExceptionResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ProductNotFoundException;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.UserNotFoundException;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Customer;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Product;
import com.springBootIntro.CustomerProductAuthenticationAPI.repository.ProductRepository;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.CreateCustomerRequest;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.CreateProductRequest;
import com.springBootIntro.CustomerProductAuthenticationAPI.repository.CustomerRepository;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.UpdateCustomerRequest;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    Logger logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;


    public List<Customer> getAllCustomers(){
        try{
            if(customerRepository.findAll()==null){
                return null;
            }
            else{
                List<Customer> customers= customerRepository.findAll();
                logger.info("Level: 1"+ customers);
                //Pageable pageable=PageRequest.of(pageNo-1,pageSize);
                return customerRepository.findAll();
            }
        }catch(Exception ex) {

            throw new RuntimeException("There is no Customer in the Database");

        }

    }

    public Customer getCustomerById(UUID id){
        try{
            Customer customer=customerRepository.findById(id).get();
            if(customer!=null){
                return customer;
            }
            else{
                throw new UserNotFoundException("Customer: "+id+" Not found in the db");
            }

        }catch (ExceptionResponse e){
            throw new ExceptionResponse(new Date(),e.getMessage(),e.getDetails());
        }

    }

    public Customer createCustomer(CreateCustomerRequest createCustomerRequest){
        try{
            if(createCustomerRequest!=null){
                Customer customer= new Customer(createCustomerRequest);
                customer=customerRepository.save(customer);
                List<Product> products=new ArrayList<Product>();
                logger.info(customer.toString());
                logger.info(createCustomerRequest.getProductsList().toArray().toString());
                if(createCustomerRequest.getProductsList()!= null){
                    for(CreateProductRequest createProductRequest: createCustomerRequest.getProductsList()){
                        Product product=new Product();
                        product.setTitle(createProductRequest.getTitle());
                        product.setDescription(createProductRequest.getDescription());
                        product.setPrice(createProductRequest.getPrice());
                        product.setCreated_at(LocalDateTime.now());
                        product.setIs_deleted(false);
                        product.setModified_at(LocalDateTime.now());
                        product.setCustomer(customer);
                        products.add(product);
                        logger.info(customer.toString());
                    }
                    logger.info(products.toString());
                    //List<Product> saved_products
                    products= productRepository.saveAll(products);
                    logger.info(products.toString());
                }
                customer.setProducts(products);
                return customer;

            }
            else{
                throw new ExceptionResponse(new Date(), "Bad Request Format", "Internal Server could");
            }

        }catch(ExceptionResponse ex){
            throw new ExceptionResponse(new Date(), ex.getMessage(), ex.getDetails());
        }

    }
/*
* try{} catch (ExceptionResponse e){
            throw new ExceptionResponse(new Date(),e.getMessage(),e.getDetails());
        }
* */

    public Customer updateCustomer(UpdateCustomerRequest updateCustomerRequest){
        try{
            Customer customer=customerRepository.findById(updateCustomerRequest.getId()).get();
            if(customer!=null){
                if(updateCustomerRequest.getTitle()!=null && !updateCustomerRequest.getTitle().isEmpty()){
                    customer.setTitle(updateCustomerRequest.getTitle());
                    customer.setModified_at(LocalDateTime.now());
                }
                customer=customerRepository.save(customer);
                logger.info(customer.getTitle());
                return customer;
            }
            else{
                SQLException sqlException=new SQLException();
                JDBCException jdbcException=new JDBCException("DB Error",sqlException);
                throw new ExceptionResponse(new Date(), jdbcException.getMessage(), jdbcException.getSQLException().getMessage());
            }

        }
        catch (ExceptionResponse e){
            throw new ExceptionResponse(new Date(),e.getMessage(),e.getDetails());
        }
    }



    public Boolean deleteCustomerById(UUID id){

        try{
            Customer customer=customerRepository.findById(id).get();
            List<Product> ProductswithSameCustomer=productRepository.findByCustomer_id(customer.getId());
            if(ProductswithSameCustomer!=null || !ProductswithSameCustomer.isEmpty()){
                for(Product product: ProductswithSameCustomer){
                    productRepository.deleteById(product.getId());
                    }
                }
            else{
                throw new ProductNotFoundException("Customer id: "+ customer.getId()+"Not Found in the DB" );
            }
        customerRepository.delete(customer);
        return true;
        }
        catch(Exception ex){
            throw new ExceptionResponse(new Date(),ex.getLocalizedMessage(),ex.getCause().getMessage());
        }
    }
/*
    public Customer updateCustomer(UpdateCustomerRequest updateCustomerRequest){

        Customer Customer=studentRepository.findById(updateStudentRequest.getId()).get();

        if(updateStudentRequest.getFirst_name() !=null && !updateStudentRequest.getFirst_name().isEmpty()){
            student.setFirstName(updateStudentRequest.getFirst_name());
        }
        student= studentRepository.save(student);
        return student;
    }

    public String deleteStudent(Long id){
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
    public Integer updateStudentWithJpql(@PathVariable Long id, @PathVariable String firstName){
        return studentRepository.updateFirstName(id, firstName);
    }

    public List<Student> getByAddressCity(String city){

        List<Student> students= studentRepository.getByAddressCity(city);
        return students;

    }
*/

}
