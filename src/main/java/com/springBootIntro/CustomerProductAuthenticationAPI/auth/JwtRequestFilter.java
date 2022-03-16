package com.springBootIntro.CustomerProductAuthenticationAPI.auth;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

    Logger logger= LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    private AdminDetailsService adminDetailsService;

    @Autowired
    private JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String loginHeader=request.getHeader("Authorization");
        String username=null;
        String jwt_token=null;
        logger.info("lo"+loginHeader);
        if(loginHeader!=null && loginHeader.startsWith("Bearer ")){
            logger.info("I got bearer Token"+loginHeader);
            jwt_token=loginHeader.substring(7);
            username=jwtUtil.extractUserName(jwt_token);
        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=this.adminDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt_token,userDetails)){
                logger.info("I am validated");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            logger.info(jwt_token);
        }
        filterChain.doFilter(request,response);
    }
}
