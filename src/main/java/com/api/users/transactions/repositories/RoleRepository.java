package com.api.users.transactions.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.users.transactions.enums.RoleName;
import com.api.users.transactions.models.RoleModel;


@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByRoleName(RoleName roleName);
}