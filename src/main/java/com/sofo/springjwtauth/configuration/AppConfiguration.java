package com.sofo.springjwtauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Created by georges on 5/10/18.
 */
@Configuration
public class AppConfiguration {

    /**
     * Bcrypt password encoder bean.
     *
     * @return the Bcrypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
