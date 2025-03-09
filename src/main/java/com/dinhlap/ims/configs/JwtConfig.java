package com.dinhlap.ims.configs;

import com.dinhlap.ims.securities.JwtAuthenticationFilter;
import com.dinhlap.ims.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class JwtConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil,
                                                           UserDetailsManager userDetailsManager) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsManager);
    }
}
