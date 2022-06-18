package com.example.newsrestapi.security;


import com.example.newsrestapi.filter.CustomAuthenticationFilter;
import com.example.newsrestapi.filter.CustomAuthorizationFilter;
import com.example.newsrestapi.utils.TokenUtil;
import com.example.newsrestapi.utils.enums.RolesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        // passing service which implements user + roles finding and its password encryption
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin"));
        // nie można wpisać '*' z jakiegoś powodu
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Access-Control-Allow-Origin"));

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), new TokenUtil());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login"); // change /login to /api/login
        // disable csrf, and cors
        http.csrf().disable().cors().configurationSource(request -> corsConfiguration);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // order matters write permitall first!
//        http.authorizeRequests().antMatchers("/api/users/").permitAll();

        // dostęp dla wszystkich
        http.authorizeRequests().antMatchers("/api/login/**", "/api/refreshtoken/**").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/users/save", "/api/refreshtoken/**").permitAll();

        // ustaw dostęp tylko dla admina
        http.authorizeRequests().antMatchers(GET, "/api/users/**").hasAnyAuthority(RolesEnum.ROLE_ADMIN.toString());
        http.authorizeRequests().antMatchers(POST, "/api/users/**").hasAnyAuthority(RolesEnum.ROLE_ADMIN.toString());
        http.authorizeRequests().antMatchers(POST, "/api/users/save").permitAll();

        http.authorizeRequests().antMatchers(GET, "/api/adminpanel/**").hasAnyAuthority(RolesEnum.ROLE_ADMIN.toString());


//      http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
