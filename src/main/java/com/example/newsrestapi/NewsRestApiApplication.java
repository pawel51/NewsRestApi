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

            AppUser admin = new AppUser(null, "johnT@wp.pl", "admin",  "1234", new ArrayList<>(), new ArrayList<>());
            userService.saveUser(admin);
            AppUser manager = new AppUser(null, "butch@protonmail.com", "manager", "1234", new ArrayList<>(), new ArrayList<>());
            userService.saveUser(manager);
            AppUser user1 = new AppUser(null,"kitty1234@o2.pl",  "user1" , "1234", new ArrayList<>(), new ArrayList<>());
            userService.saveUser(user1);
            userService.saveUser(new AppUser(null,"kitty12345@o2.pl",  "user2" , "1234", new ArrayList<>(), new ArrayList<>()));

            userService.addRoleToUser("admin", ROLE_ADMIN.toString());
            userService.addRoleToUser("admin", ROLE_MANAGER.toString());
            userService.addRoleToUser("manager", ROLE_MANAGER.toString());
            userService.addRoleToUser("user1", ROLE_USER.toString());
            userService.addRoleToUser("user2", ROLE_USER.toString());

            Category categoryJobs = new Category(null, "Jobs", new ArrayList<>());
            categoryService.create(categoryJobs);
            Category categoryNews = new Category(null, "News", new ArrayList<>());
            categoryService.create(categoryNews);
            Category categoryOffer = new Category(null, "Offers", new ArrayList<>());
            categoryService.create(categoryOffer);

            Date tomorrow = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(tomorrow);
            c.add(Calendar.DATE, 1);
            tomorrow = c.getTime();
            announcementService.create(new Announcement(null, "Paint 3D UI designer", "See the title", new Date(), tomorrow,
                    AnnouncementState.NotPublic, admin, categoryJobs));
            announcementService.create(new Announcement(null, "Opel Astra for sale", "nice car", new Date(), tomorrow,
                    AnnouncementState.NotPublic, manager, categoryOffer));
            announcementService.create(new Announcement(null, "D.Trump chosen for president of USA", "D.Trump chosen for president of USA second time", new Date(), tomorrow,
                    AnnouncementState.NotPublic, user1, categoryNews));
        };
    }
}
