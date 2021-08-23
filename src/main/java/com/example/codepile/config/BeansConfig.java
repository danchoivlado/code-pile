package com.example.codepile.config;

import com.example.codepile.data.converters.AceConverter;
import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.factories.PileFactory;
import com.example.codepile.data.models.binding.user.EditProfieBindingModel;
import com.example.codepile.data.models.service.pile.MyPileServiceViewModel;
import com.example.codepile.data.models.service.pile.MyPilesServiceViewModel;
import com.example.codepile.data.models.view.piles.MyPileViewModel;
import com.example.codepile.data.models.view.piles.MyPilesViewModel;
import com.example.codepile.services.AlphanumericString;
import com.example.codepile.services.services.RandomAlphanumericStringGenerator;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.util.Arrays;

@Configuration
public class BeansConfig {
    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();

        Converter<AceMode, String> aceModeStringConverter =
                ctx -> ctx.getSource().getId();

        modelMapper.createTypeMap(MyPileServiceViewModel.class, MyPileViewModel.class)
                .addMappings(map -> map
                        .using(aceModeStringConverter)
                        .map(
                                MyPileServiceViewModel::getAceMode,
                                MyPileViewModel::setLanguage
                        )
                );
    }

    @Bean
    public AlphanumericString alphanumericString(){
        return new RandomAlphanumericStringGenerator();
    }

    @Bean(name = "pile")
    public PileFactory pileFactory() {
        PileFactory factory = new PileFactory();
        factory.setFactoryId(7070);
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

