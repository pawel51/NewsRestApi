package com.example.newsrestapi.api;

import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Role;
import com.example.newsrestapi.service.UserService;
import com.example.newsrestapi.utils.TokenUtil;
import com.example.newsrestapi.utils.enums.RolesEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.RoleDTO;
import dto.UserDto;
import dto.UserRegistrationDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TokenUtil tokenUtil;



    // Odnawia access_token używając do tego refresh_token
    @GetMapping("/refreshtoken")
    public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("refresh token procedure started");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
//                TokenUtil tokenUtil = new TokenUtil();
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                // if exception not threw user is already autenticated
                String username = tokenUtil.getUserRoles(refreshToken).getUserName();
                AppUser user = userService.getUser(username);
                List<String> roles = user.getRoles().stream()
                        .map(Role::getName)
                        .map(RolesEnum::toString)
                        .collect(Collectors.toList());

                String accessToken = tokenUtil.GetToken(request, user, roles, 1000);
                tokenUtil.packTokensToFront(response, refreshToken, accessToken);

            } catch (Exception exception) {
                log.error("error loggin in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                // send error forbiden 403 or
//                    response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }


    // pobiera wszystkich użytkowników
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(){
        log.info("get all users from database");

        return ResponseEntity.ok().body(
                userService.getUsers()
                        .stream()
                        .map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList())
        );
    }

    @GetMapping("/users/edit/{username}")
    public ResponseEntity <UserDto> editUser(@PathVariable String username) {
        log.info("get users with specific username from database");

        return ResponseEntity.ok().body(
                modelMapper.map(userService.getUser(username), UserDto.class)
        );
    }

    // zapisuje użytkownika do bazy (rejestracja)
    @PostMapping("/users/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody UserRegistrationDTO user){
        log.info("save user to database");

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());

        // Set user default role to user
        RoleDTO userRole = new RoleDTO(1L, "ROLE_USER");
        Role mappedUserRole = modelMapper.map(userRole, Role.class);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(mappedUserRole);

        AppUser appUser = new AppUser(null, user.getEmail(), user.getUsername(), user.getPassword(), roles, new ArrayList<>());

        return ResponseEntity.created(uri).body(userService.saveUser(appUser));
    }

    // dodawanie roli przez endpoint (trzeba updatować RolesEnum przy dodawaniu)
    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        log.info("add role for user using endpoint");

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @GetMapping("/roles")
    public Collection<Role> getUserRoles(Principal principal){
        AppUser user = userService.getUser(principal.getName());

        return user.getRoles();
    }

    @GetMapping("/user")
    public long getUserId(Principal principal){
        AppUser user = userService.getUser(principal.getName());
        return user.getId();
    }

    // przypisanie roli do użytkownika
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRolToUser(@RequestBody RoleToUserForm form){
        log.info("add role for user");

        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }

    // przypisanie roli do użytkownika
//    @PutMapping ("/role/addtouser/{username}")
//    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @RequestBody ArrayList roles){
//
//        for (int i = 0; i < roles.toArray().length; i++) {
//            userService.addRoleToUser(userService.getUser(username).toString(), roles.get(i).toString());
//        }
//
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/role/addtouser/{username}")
    public ResponseEntity <AppUser> editUser(@PathVariable String username, @RequestBody UserDto user) {

        AppUser appUser = userService.getUser(user.getUsername());
        if(appUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else {
            AppUser modifiedUser = modelMapper.map(user, AppUser.class);
            modifiedUser.setPassword(appUser.getPassword());

            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(modifiedUser));
        }

    }


}

@Data
class RoleToUserForm {
    private String username;
    private String rolename;
}