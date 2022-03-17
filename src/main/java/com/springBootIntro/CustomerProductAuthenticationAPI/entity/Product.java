package com.springBootIntro.CustomerProductAuthenticationAPI.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

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

    @Column(name="description",length = 1024)
    @Size(min = 0,max = 1024)
    private String description;

    @Column(name = "price")
    @Min(2)
    @Max(10)
    private int price;

    @Column(name = "is_deleted")
    private Boolean is_deleted;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "modified_at")
    private LocalDateTime modified_at;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

}
