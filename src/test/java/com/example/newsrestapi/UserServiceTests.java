package com.example.newsrestapi;


import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.model.Role;
import com.example.newsrestapi.service.AnnouncementService;
import com.example.newsrestapi.service.CategoryService;
import com.example.newsrestapi.service.UserService;
import com.example.newsrestapi.utils.enums.RolesEnum;
import dto.RoleDTO;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    CategoryService categoryService;
    @Autowired
    AnnouncementService announcementService;
    @Autowired
    UserService userService;



    @Test
    public void create_ShouldCreateUser() {

        //arrange
        AppUser appUser = createUser();

        //act
        userService.saveUser(appUser);

        //assert
        Assertions.assertNotNull(appUser.getId());

    }

    @Test
    public void getUser_ShouldFindUser() {
        //arrange
        AppUser appUser = createUserAndSaveToDB();

        //act
        AppUser appUserFromDB = userService.getUser(appUser.getUsername());

        //assert
        Assertions.assertEquals(appUser.getId(), appUserFromDB.getId());
    }

    @Test
    public void getUserById_ShouldFindUser() {
        //arrange
        AppUser appUser = createUserAndSaveToDB();

        //act
        AppUser appUserFromDB = userService.getUserById(appUser.getId());

        //assert
        Assertions.assertEquals(appUser.getId(), appUserFromDB.getId());
    }

    @Test
    public void getUsers_ShouldFindUser() {
        //arrange
        List<AppUser> appUsersBefore = userService.getUsers();
        int sizeBefore = appUsersBefore.size();

        AppUser appUser1 = createUserAndSaveToDB();
        AppUser appUser2 = createUserAndSaveToDB();
        AppUser appUser3 = createUserAndSaveToDB();


        //act
        List<AppUser> appUsersAfter = userService.getUsers();

        //assert
        int expectedValue = sizeBefore + 3;
        Assertions.assertEquals(expectedValue, appUsersAfter.size());
    }

    @Test
    public void updateUser_ShouldCreateRole() {
        //arrange
        AppUser appUser = createUser();
        Collection<Role> r = createListUserManagerRole();
        appUser.setRoles(r);

        //act
        userService.updateUser(appUser);

        //assert
        Assertions.assertEquals(2, userService.getUserById(appUser.getId()).getRoles().size());

    }

    @Test
    public void saveRole_ShouldCreateRole() {
        //arrange
        Role role = new Role(null, RolesEnum.ROLE_USER);

        //act
        userService.saveRole(role);

        //assert
        Assertions.assertNotNull(role.getId());

    }


    private AppUser createUser() {

        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        appUser.setPassword("password");
        appUser.setEmail("testUser@test.com");


        ModelMapper modelMapper = new ModelMapper();
        RoleDTO userRole = new RoleDTO(1L, "ROLE_USER");
        Role mappedUserRole = modelMapper.map(userRole, Role.class);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(mappedUserRole);

        return appUser;
    }

    private AppUser createUserAndSaveToDB()
    {
        AppUser appUser = createUser();
        return userService.saveUser(appUser);
    }

    private List<Role> createListUserManagerRole() {

        ModelMapper modelMapper = new ModelMapper();

        RoleDTO userRole = new RoleDTO(1L, "ROLE_USER");
        Role mappedUserRole = modelMapper.map(userRole, Role.class);

        RoleDTO managerRole = new RoleDTO(2L, "ROLE_MANAGER");
        Role mappedManagerRole = modelMapper.map(managerRole, Role.class);

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(mappedUserRole);
        roles.add(mappedManagerRole);

        return roles;

    }


}
