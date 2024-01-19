package com.company;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TicketingProjectRest {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectRest.class, args);
    }

    @Bean
    public ModelMapper mapper(){ // To be able to use ModelMapper, we need Spring to create a bean from it.
        return new ModelMapper();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        /*
         * 🖍️...
         * · Spring never accepts a regular password. It always accepts encoded passwords.
         * · We use the PasswordEncoder that is defined in the Spring Security configuration to encode the password.
         * · BCryptPasswordEncoder class is implemented from the PasswordEncoder interface.
         */
    }




}
