package com.example.codepile.config;

import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.factories.PileFactory;
import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import com.example.codepile.services.AlphanumericString;
import com.example.codepile.services.services.RandomAlphanumericStringGenerator;
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
    public AlphanumericString alphanumericString(){
        return new RandomAlphanumericStringGenerator();
    }

    @Bean
    public PileFactory pileFactory() {
        PileFactory factory = new PileFactory();
        factory.setFactoryId(7070);
        factory.setPileId(alphanumericString().getRandomAlphanumericString());
        factory.setPileText("");
        factory.setAceMode(AceMode.JAVASCRIPT);
        factory.setTitle(factory.getPileId());
        factory.setReadOnly(true);
        return factory;
    }

    @Bean
    public Pile pile() throws Exception {
        return pileFactory().getObject();
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

