package com.springBootIntro.CustomerProductAuthenticationAPI.Controller;

import com.springBootIntro.CustomerProductAuthenticationAPI.Exception.ExceptionResponse;
import com.springBootIntro.CustomerProductAuthenticationAPI.auth.AdminDetailsService;
import com.springBootIntro.CustomerProductAuthenticationAPI.auth.JWTUtil;
import com.springBootIntro.CustomerProductAuthenticationAPI.request.AuthenticationRequest;
import com.springBootIntro.CustomerProductAuthenticationAPI.response.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class AuthController {
    Logger logger= LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager loginManager;
    @Autowired
    private AdminDetailsService adminDetailsService;
    @Autowired
    private JWTUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createLoginToken(@RequestBody AuthenticationRequest loginRequest) throws ExceptionResponse{
        try {
            loginManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            throw  new ExceptionResponse(new Date(),"Incorrect Credential",e.getCause().getMessage());
        }
        final UserDetails userDetails=adminDetailsService.loadUserByUsername(loginRequest.getUsername());
        logger.info("User"+userDetails.getUsername()+userDetails.getPassword());
        final String jwt=jwtTokenUtil.genarateToken(userDetails);
        logger.info(jwt);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    @GetMapping("/hello")
    public String helloRoute(){
        return "Hello";
    }

}
