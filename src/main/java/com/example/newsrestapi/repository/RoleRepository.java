package com.example.newsrestapi.repository;

import com.example.newsrestapi.model.Role;
import com.example.newsrestapi.utils.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RolesEnum roleName);
}
