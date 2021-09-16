package com.kms.seft203;

import java.util.ArrayList;
import java.util.Optional;

import com.kms.seft203.domain.app.AppVersion;
import com.kms.seft203.domain.app.AppVersionRepository;
import com.kms.seft203.domain.auth.UserEntity;
import com.kms.seft203.domain.auth.UserRepository;
import com.kms.seft203.domain.dashboard.Dashboard;
import com.kms.seft203.domain.dashboard.DashboardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "com.kms.seft203")
public class Seft203Application {

    @Autowired
	PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(Seft203Application.class, args);
    }

    @Bean
    CommandLineRunner runner(
            AppVersionRepository appVersionRepository, 
            UserRepository userRepository,
            DashboardRepository dashboardRepository
        ){
        return args -> {
            appVersionRepository.save(new AppVersion(1L, "SEFT Program", "1.0.0"));
            
            dashboardRepository.save(new Dashboard("1L", "1", "title", "layoutType", new ArrayList<>()));
            Optional<UserEntity> user = userRepository.findByUsername("admin");
            if(!user.isPresent()){
                userRepository.save(new UserEntity("1L", "admin", encoder.encode("admin"), "admin@gmail.com", "Admin", true));
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
                        .allowedOrigins("*");
            }
        };
    }
}
