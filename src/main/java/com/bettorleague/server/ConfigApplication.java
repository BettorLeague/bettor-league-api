package com.bettorleague.server;

import com.bettorleague.server.model.security.AuthProvider;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.repository.security.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfigApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        log.warn("Loading admin account...");

        //elasticsearchTemplate.createIndex("test");
        if(userRepository.existsByEmail("admin@admin.com")) {
            log.warn("Admin already added");
        }else{
            User user = new User();
            user.setName("admin");
            user.setEmail("admin@admin.com");
            user.setPassword("admin");
            user.setProvider(AuthProvider.local);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.warn("Admin account added");
        }
    }

}

