package com.springBootIntro.CustomerProductAuthenticationAPI.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.CreateCustomerRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @Column(name="title",length = 255)
    @Size(min = 0,max = 255)
    private String title;

    @Column(name="is_deleted")
    private Boolean is_deleted;

    @Column(name="created_at")
    private LocalDateTime created_at;

    @Column(name="modified_at")
    private LocalDateTime modified_at;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Product> products;

    public Customer(CreateCustomerRequest createCustomerRequest){
        this.title=createCustomerRequest.getTitle();
        this.is_deleted=false;
        this.created_at=LocalDateTime.now();
        this.modified_at=LocalDateTime.now();

    }
}
