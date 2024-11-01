package org.example.xpresbank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.xpresbank.Entity.Role;
import org.example.xpresbank.Entity.Enums.RoleType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
