package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Role;
import com.eCommerce.eCommerce.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
