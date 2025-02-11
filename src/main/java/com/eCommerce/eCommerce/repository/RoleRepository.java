package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Role;
import com.eCommerce.eCommerce.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
