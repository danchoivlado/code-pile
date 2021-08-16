package com.example.codepile.config;

import com.example.codepile.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String CSRF_ATTRIBUTE_NAME = "_csrf";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .csrfTokenRepository(csrfTokenRepository)
                .and()
                .authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/images/**").permitAll()
                .antMatchers("/", "/users/register", "/users/login").anonymous()
                .antMatchers("/api/users/checkUserExistsWithUsername").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/");
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}
