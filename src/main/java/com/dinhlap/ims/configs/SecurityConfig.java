package com.dinhlap.ims.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    @Value("${jwt.signing.key}")
    private String signerKey;

    private final String[] PUBLIC_ENDPOINTS = {"/api/auth/token",
                                            "/api/auth/introspect"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        userDetailsManager.setUsersByUsernameQuery("select username, password, " +
//                "CASE WHEN status = 'Active' THEN 1 ELSE 0 END as enabled from Users where username=?");
//        userDetailsManager.setAuthoritiesByUsernameQuery("select username, role as authority from Users where username=?");
//
//        return userDetailsManager;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http. csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(configurer ->
//                        configurer.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//                                .requestMatchers("/auth/**", "/api/auth/**").permitAll()
//                                .requestMatchers("/api/users/**").permitAll()
//                                .requestMatchers("/**").permitAll()
//                                .requestMatchers("/users/**", "/api/users/updateStatus").hasAuthority("Admin")
//                                .requestMatchers("/api/users/current-user-fullname").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .requestMatchers("/candidates/create", "/candidates/delete/**").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .requestMatchers("/interview-schedule/add").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .requestMatchers("/interview-schedule/edit/**").hasAnyAuthority("Admin", "Manager", "Recruiter", "Interviewer")
//                                .requestMatchers("/interview-schedule/scheduleDetail/**").hasAnyAuthority("Admin", "Manager",  "Recruiter", "Interviewer")
//                                .requestMatchers("/interview-schedule/cancel/**").hasAnyAuthority("Admin", "Recruiter")
//                                .requestMatchers("/jobs/create", "/jobs/createjob").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .requestMatchers("/jobs/edit/**").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .requestMatchers("/jobs/delete/**").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .requestMatchers("/offers").hasAnyAuthority("Admin", "Manager", "Recruiter")
//                                .anyRequest().authenticated())
//                .formLogin(login ->
//                        login.loginPage("/auth/login")
//                                .loginProcessingUrl("/authenticateUser")
//                                .permitAll())
//                .rememberMe(rememberMe -> rememberMe
//                        .tokenValiditySeconds(86400)
//                        .key("uniqueAndSecret")
//                        .rememberMeParameter("rememberMe"))
//                .logout(logout ->
//                        logout.logoutUrl("/logout")
//                                .logoutSuccessUrl("/auth/login?logout")
//                                .permitAll()
//                );
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/users/**", "/api/users/**").hasAuthority("ROLE_Admin")
                        .anyRequest().authenticated()
        );

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
