package com.springBootIntro.CustomerProductAuthenticationAPI.response;

import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {


    private UUID id;

    private String title;

    private String description;

    private int price;

    public ProductResponse(Product product){
        this.id=product.getId();
        this.title=product.getTitle();
        this.description= product.getDescription();
        this.price= product.getPrice();

    }

}
