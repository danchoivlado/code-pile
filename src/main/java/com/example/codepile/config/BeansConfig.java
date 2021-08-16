package com.example.codepile.config;

import com.example.codepile.data.entities.User;
import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class BeansConfig {
    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
//        modelMapper.addMappings(new PropertyMap<User, EditProfieBindingModel>() {
//            @Override
//            protected void configure() {
//                skip(destination.getPassword());
//            }
//        });

    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName(WebSecurityConfig.CSRF_ATTRIBUTE_NAME);
        return repository;
    }
}

