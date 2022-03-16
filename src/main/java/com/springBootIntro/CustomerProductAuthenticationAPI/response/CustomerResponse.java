package com.springBootIntro.CustomerProductAuthenticationAPI.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Customer;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Product;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CustomerResponse {

    private UUID id;

    @JsonProperty
    private String title;

    private List<ProductResponse> productResponses;

    private HttpStatus httpStatus;

    public CustomerResponse(Customer customer) {
        this.id=customer.getId();
        this.title=customer.getTitle();

        if(customer.getProducts()!=null){
            productResponses=new ArrayList<ProductResponse>();
            for (Product product: customer.getProducts()){
                productResponses.add(new ProductResponse(product));
            }

        }


    }
}
