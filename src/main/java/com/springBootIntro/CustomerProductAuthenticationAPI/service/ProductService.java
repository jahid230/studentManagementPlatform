package com.springBootIntro.CustomerProductAuthenticationAPI.service;

import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ExceptionResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ProductNotFoundException;
import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.UserNotFoundException;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Customer;
import com.springBootIntro.CustomerProductAuthenticationAPI.entity.Product;
import com.springBootIntro.CustomerProductAuthenticationAPI.repository.CustomerRepository;
import com.springBootIntro.CustomerProductAuthenticationAPI.repository.ProductRepository;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.CreateProductRequest;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    Logger logger= LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;

    public List<Product> getAllProducts(UUID customer_id){
        try{
            List<Product> products=productRepository.findByCustomer_id(customer_id);
            if(products!=null){
                return products;
            }
            else if(products.isEmpty()){
                return null;
            }
            else{
                SQLException sqlException=new SQLException();
                JDBCException jdbcException=new JDBCException("DB Error",sqlException);
                throw new ExceptionResponse(new Date(), jdbcException.getMessage(), jdbcException.getSQLException().getMessage());
            }
        }
        catch(Exception ex){
            throw new ExceptionResponse(new Date(),ex.getLocalizedMessage(),ex.getCause().getMessage());
        }
    }

    public Product createProductByCustomerId( UUID id, CreateProductRequest productRequest){
        try{
            Customer customer=customerRepository.findById(id).get();
            logger.info("ProductLevel 1:"+customer.toString());
            logger.info("ProductLevel 12:"+productRequest.toString());
            Product product=new Product();
            if(customer!=null&& !customer.toString().isEmpty()){
                product.setIs_deleted(false);
                product.setCreated_at(LocalDateTime.now());
                product.setModified_at(LocalDateTime.now());
                product.setTitle(productRequest.getTitle());
                product.setDescription(productRequest.getDescription());
                product.setCustomer(customer);
                product=productRepository.save(product);
            }
            else {
                throw new UserNotFoundException("Customer Not Found");
            }

            if(product ==null){
                SQLException sqlException=new SQLException();
                JDBCException jdbcException=new JDBCException("DB Error",sqlException);
                throw new ExceptionResponse(new Date(), jdbcException.getMessage(), jdbcException.getSQLException().getMessage());
            }
            return product;
        }
        catch(ExceptionResponse ex){
            throw new ExceptionResponse(new Date(),ex.getLocalizedMessage(),ex.getCause().getMessage());
        }
    }

    public Boolean deleteProductById(UUID id) {

        try{
            Product product=productRepository.findById(id).get();
            UUID customer_id=product.getCustomer().getId();
            Customer customer=customerRepository.findById(customer_id).get();
            logger.info(customer.getId().toString());
            if(customer!=null){
                List<Product> products=new ArrayList<>();
                customer.getProducts().stream().forEach(nproduct -> {
                    if(!nproduct.getId().equals(id)){
                        products.add(nproduct);
                    }
                });
                customer.setProducts(products);
                customerRepository.save(customer);
                productRepository.deleteById(id);
                return true;
            }
            else{
                throw new UserNotFoundException("Customer Not Found in the DB");
            }


        }catch (ExceptionResponse ex){
            throw new ExceptionResponse(new Date(), ex.getMessage(), ex.getDetails());
        }
    }

    public Product updateProductByProductId(UUID id, CreateProductRequest productRequest) {

        try {
            Product product = productRepository.findById(id).get();
            UUID customer_id = product.getCustomer().getId();
            Customer customer = customerRepository.findById(customer_id).get();
            if (product != null) {
                List<Product> products = new ArrayList<>();
                if (productRequest.getTitle().isEmpty() || productRequest.getTitle() != null
                        || productRequest.getDescription().isEmpty() || productRequest.getDescription() != null || productRequest.getPrice() != 0) {
                    product.setPrice(product.getPrice());
                    product.setTitle(productRequest.getTitle());
                    product.setDescription(productRequest.getDescription());
                    product.setModified_at(LocalDateTime.now());
                    customer.getProducts().stream().forEach(nproduct -> {
                        if (nproduct.getId().equals(id)) {
                            nproduct.setPrice(product.getPrice());
                            nproduct.setTitle(productRequest.getTitle());
                            nproduct.setDescription(productRequest.getDescription());
                            nproduct.setModified_at(LocalDateTime.now());
                            products.add(nproduct);
                        }
                        products.add(product);
                    });
                    customer.setProducts(products);
                    return  productRepository.save(product);

                } else {
                    throw new ProductNotFoundException("Product Not Found");
                }
            }


        } catch (ExceptionResponse ex) {
            throw new ExceptionResponse(new Date(), ex.getMessage(), ex.getDetails());
        }
        return null;
    }
}
