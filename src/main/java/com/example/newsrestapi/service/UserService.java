package com.example.newsrestapi.service;

import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Role;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();

    AppUser getUserById (Long userID);
}
