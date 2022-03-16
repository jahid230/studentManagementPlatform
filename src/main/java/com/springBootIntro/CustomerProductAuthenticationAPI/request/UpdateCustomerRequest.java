package com.springBootIntro.CustomerProductAuthenticationAPI.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {


    //@NotNull(message="Customer Id required")
    @JsonIgnore
    private UUID id;
    private String title;

}
