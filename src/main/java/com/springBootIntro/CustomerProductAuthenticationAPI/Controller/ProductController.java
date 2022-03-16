package com.springBootIntro.CustomerProductAuthenticationAPI.Controller;

import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.BadRequestFormat;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ExceptionResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ProductNotFoundException;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Product;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.CreateProductRequest;
import com.springBootIntro.CustomerProductAuthenticationAPI.response.ProductResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;
    @GetMapping("/customers/{CustomerId}/products")
    public CollectionModel<ProductResponse> getAllProductsViaCustomerId(@PathVariable(name = "CustomerId") UUID id) {
        try {
            List<Product> productList=productService.getAllProducts(id);
            List<ProductResponse> allResponses = new ArrayList<>();

            productList.stream().forEach(product ->
            {
                allResponses.add(new ProductResponse(product));

            });

            CollectionModel<ProductResponse> model=CollectionModel.of(allResponses);
            return CollectionModel.of(allResponses);

        } catch (ExceptionResponse ex) {
            throw new ExceptionResponse(new Date(), ex.getLocalizedMessage(), ex.getCause().getMessage());

        }

    }
    @PostMapping("/customers/{CustomerId}/products")

    public EntityModel<ProductResponse> createNewProductByCustomer(@PathVariable(name="CustomerId") UUID id,@Valid @RequestBody CreateProductRequest productRequest){
        try {
            logger.info("Controller Info: "+ id+" "+productRequest.toString());
            if(productRequest!=null&& !productRequest.toString().isEmpty()){
                Product product=productService.createProductByCustomerId(id,productRequest);
                if(product.getId()!=null){
                    WebMvcLinkBuilder linktoUpdate=WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).updateProductById(id,productRequest));
                    WebMvcLinkBuilder linktoAll=WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllProductsViaCustomerId(id));
                    ProductResponse productResponse=new ProductResponse(product);
                    EntityModel<ProductResponse> model=EntityModel.of(productResponse);
                    model.add(linktoAll.withRel("All Products Url"));
                    model.add(linktoUpdate.withRel("Update Url for the Product"));
                    return model;
                }
                else {
                    throw new ProductNotFoundException("Product not found  for the customer Id:"+ id);
                }

            }else{
                throw new BadRequestFormat("Bad Request Format");
            }

        }catch (Exception ex){
            throw new ExceptionResponse(new Date(), ex.getLocalizedMessage(), ex.getCause().getMessage());
        }
    }
    @DeleteMapping("/products/{ProductId}")

    public String DeleteProductById(@PathVariable(name = "ProductId") UUID id){
        try{
           if(productService.deleteProductById(id)){
               return "Product Deleted Successfully";
           }
           else{
               return "Product Did not delete";
           }

        }catch(ExceptionResponse ex){
            throw new ExceptionResponse(new Date(), ex.getMessage(), ex.getDetails());
        }
    }

    @PutMapping("/products/{ProductId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public EntityModel<ProductResponse> updateProductById(@PathVariable("ProductId") UUID id, @RequestBody CreateProductRequest productRequest){
        try{
            Product product=productService.updateProductByProductId(id, productRequest);
            ProductResponse productResponse=new ProductResponse(product);
            EntityModel<ProductResponse> productEntityModel=EntityModel.of(productResponse);
                WebMvcLinkBuilder linktoUser=WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                        CustomerContoller.class).getCustomerById(product.getCustomer().getId()));
            productEntityModel.add(linktoUser.withRel("customer-details of the product"));
            return productEntityModel;
        }catch(ExceptionResponse ex){
            throw  new ExceptionResponse(new Date(), ex.getMessage(), ex.getDetails());
        }

    }

}
