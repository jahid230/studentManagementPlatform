package com.springBootIntro.CustomerProductAuthenticationAPI.Controller;

import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ExceptionResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.UserNotFoundException;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Customer;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.CreateCustomerRequest;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.UpdateCustomerRequest;
import com.springBootIntro.CustomerProductAuthenticationAPI.response.CustomerResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.response.ProductResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerContoller {

    // Error<Warn<Info<Debug<Trace

    Logger logger= LoggerFactory.getLogger(CustomerContoller.class);


    @Autowired
    CustomerService customerService;

    @Value("${app.name:Customer App}")
    private String appName;

    @PostMapping
    public CustomerResponse createNewCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest){
        logger.info(createCustomerRequest.toString());
        Customer customer= customerService.createCustomer(createCustomerRequest);
        if(customer==null){
            JDBCException jdbcException=new JDBCException("Detailed exception can be debug via sql exception",new SQLException());
            throw new ExceptionResponse(new Date(),"No Customer Created","Details: "+jdbcException.getSQLException().getMessage());
        }
        else{
            CustomerResponse add_response=new CustomerResponse(customer);
            add_response.setHttpStatus(HttpStatus.CREATED);
            return add_response;
        }

    }


    @GetMapping
    public Page<CustomerResponse> getAllCustomers(){
        try {
            List<Customer> customerList = customerService.getAllCustomers();
            logger.info("Level 2 : "+ customerList);
            if(customerList==null){
                throw new UserNotFoundException("No Customer in the DB");
            }
            else{
                List<CustomerResponse> allCustomerResponses = new ArrayList<>();
                for (Customer customer : customerList) {
                    CustomerResponse add_response=new CustomerResponse(customer);
                    add_response.setHttpStatus(HttpStatus.OK);
                    allCustomerResponses.add(add_response);
                }
                long totalCustomers=allCustomerResponses.stream().count();
                int pageSize= ((int) totalCustomers)/10+1;
                Pageable pageable=PageRequest.of(0,pageSize, Sort.by(Sort.Order.asc("created_at")));
                Page<CustomerResponse> pageCustomer=new PageImpl<>(allCustomerResponses,pageable,totalCustomers);
                return pageCustomer;
            }


        } catch (ExceptionResponse ex) {
            throw new ExceptionResponse(new Date(), ex.getLocalizedMessage(), ex.getDetails());

        }
    }
    @GetMapping("/{CustomerId}")
    public CustomerResponse getCustomerById(@PathVariable(name = "CustomerId") UUID id){
        try {
            Customer customer= customerService.getCustomerById(id);
            logger.info("Level 2 : "+ customer);
            if(customer==null){
                throw new UserNotFoundException("Customer: "+id +" in the DB not found");
            }
            else{
                CustomerResponse allCustomerResponse = new CustomerResponse(customer);
                allCustomerResponse.setHttpStatus(HttpStatus.OK);
                return allCustomerResponse;
            }
        } catch (ExceptionResponse ex) {
            throw new ExceptionResponse(new Date(), ex.getLocalizedMessage(), ex.getDetails());

        }
    }



    @PutMapping("/{CustomerId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public EntityModel<CustomerResponse> updateCustomer(@PathVariable(name = "CustomerId") UUID id, @Valid @RequestBody UpdateCustomerRequest customerRequest){
        logger.info(customerRequest.toString());
        try{
            customerRequest.setId(id);
            logger.info(customerRequest.toString());
            Customer customer=customerService.updateCustomer(customerRequest);

             if(customer!=null){
                 CustomerResponse customerResponse=new CustomerResponse(customer);
                 customerResponse.setHttpStatus(HttpStatus.OK);
                 logger.info(customerResponse.getTitle());
                 EntityModel<CustomerResponse> customerEntityModel=EntityModel.of(customerResponse);
                 WebMvcLinkBuilder linktoUser=WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                         this.getClass()).getAllCustomers());
                 WebMvcLinkBuilder linkToUpdate=WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).updateCustomer(id, customerRequest));
                 customerEntityModel.add(linktoUser.withRel("all-customers"));
                 customerEntityModel.add(linkToUpdate.withRel("update-info"));
                 return customerEntityModel;

            }
            else {
                throw new UserNotFoundException("Id :"+ id + "Not found");
            }

        }
        catch (Exception ex){
            throw new ExceptionResponse(new Date(),ex.getLocalizedMessage(),ex.getCause().getMessage());
        }
    }

    @DeleteMapping("/{CustomerId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> deleteCustomerById(@PathVariable(name = "CustomerId") UUID id){
        try {
            if(customerService.deleteCustomerById(id)){
                return new ResponseEntity<>("Successfully deleted the customer with id:",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Unexpected Error",HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        catch (Exception ex){
            throw new ExceptionResponse(new Date(),ex.getLocalizedMessage(),ex.getCause().getMessage());
        }
    }




}
