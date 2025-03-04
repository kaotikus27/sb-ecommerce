package com.ecommerce.sbecom.config;

import com.ecommerce.sbecom.security.services.UserDetailsServiceImpl;
import com.ecommerce.sbecom.sercurity.jwt.AuthEntryPointJwt;
import com.ecommerce.sbecom.sercurity.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @Autowired
    private AuthEntryPointJwt unathorizedHandler;


    @Bean
    public AuthTokenFilter authenticationJwtTokeFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        return new DaoAuthenticationProvider();
    }



}
