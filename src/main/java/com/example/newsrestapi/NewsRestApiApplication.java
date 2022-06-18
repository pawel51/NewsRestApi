package com.example.newsrestapi;

import com.example.newsrestapi.model.*;
import com.example.newsrestapi.service.AnnouncementService;
import com.example.newsrestapi.service.CategoryService;
import com.example.newsrestapi.service.UserService;
import com.example.newsrestapi.utils.enums.RolesEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.newsrestapi.utils.enums.RolesEnum.*;

@SpringBootApplication
@EnableScheduling
public class NewsRestApiApplication {

    public static void main (String[] args) {
        SpringApplication.run(NewsRestApiApplication.class, args);
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, CategoryService categoryService, AnnouncementService announcementService){
        return args -> {
            userService.saveRole(new Role(null, ROLE_USER));
            userService.saveRole(new Role(null, RolesEnum.ROLE_MANAGER));
            userService.saveRole(new Role(null, RolesEnum.ROLE_ADMIN));
            userService.saveRole(new Role(null, RolesEnum.ROLE_SUPERADMIN));

            AppUser appUser = new AppUser(null, "johnT@wp.pl", "John Travolta",  "1234", new ArrayList<>(), new ArrayList<>());
            userService.saveUser(appUser);
            userService.saveUser(new AppUser(null, "butch@protonmail.com", "Bruce Willic", "1234", new ArrayList<>(), new ArrayList<>()));
            userService.saveUser(new AppUser(null,"kitty1234@o2.pl",  "Katy Parry" , "1234", new ArrayList<>(), new ArrayList<>()));
            userService.saveUser(new AppUser(null, "umauma@onet.pl", "Uma Thurman" , "1234", new ArrayList<>(), new ArrayList<>()));

            userService.addRoleToUser("John Travolta", ROLE_ADMIN.toString());
            userService.addRoleToUser("John Travolta", ROLE_MANAGER.toString());
            userService.addRoleToUser("Uma Thurman", ROLE_USER.toString());
            userService.addRoleToUser("Bruce Willic", ROLE_USER.toString());

            Category category = new Category(null, "Jobs", new ArrayList<>());
            categoryService.create(category);

            Date tomorrow = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(tomorrow);
            c.add(Calendar.DATE, 1);
            tomorrow = c.getTime();
            announcementService.create(new Announcement(null, "Paint 3D UI designer", "See the title", new Date(), tomorrow,
                    AnnouncementState.NotPublic, appUser, category));
        };
    }
}
