package com.learning.springboot.authservice.repository;

import com.learning.springboot.authservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(String role);
}
