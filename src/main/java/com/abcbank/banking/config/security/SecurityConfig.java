package com.abcbank.banking.config.security;

import com.abcbank.banking.common.AuthEntryPoint;
import com.abcbank.banking.user.servives.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         29/08/17
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/counters", "/services", "/tokens").permitAll()
                .antMatchers("/tokens/**", "/counters/**").hasAnyRole("MANAGER", "OPERATOR")
                .antMatchers("/users/**").hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .httpBasic().realmName("ABCBank")
                .authenticationEntryPoint(authEntryPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
