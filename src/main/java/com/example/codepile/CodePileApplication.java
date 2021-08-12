package com.example.codepile;

import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.Authority;
import com.example.codepile.data.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class CodePileApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CodePileApplication.class, args);
//        UserRepository userRepository =  ctx.getBean(UserRepository.class);
//        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
//        List<User> s = userRepository.findAll();
//        User user = new User();
//        user.setUsername("Ester2");
//        user.setAuthority(Authority.USER);
//        user.setActive(true);
//        user.setPassword(passwordEncoder.encode("pass"));
//        userRepository.saveAndFlush(user);
    }

}
