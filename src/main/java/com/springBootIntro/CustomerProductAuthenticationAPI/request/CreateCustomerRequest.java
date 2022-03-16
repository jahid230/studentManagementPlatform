package com.springBootIntro.CustomerProductAuthenticationAPI.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @JsonProperty
    @NotBlank(message = "Title Name required")
    private String title;
    @JsonProperty
    private List<CreateProductRequest> productsList;



}
