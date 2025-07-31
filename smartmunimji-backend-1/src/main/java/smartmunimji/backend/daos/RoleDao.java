package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.Role;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role, Short> {
    Optional<Role> findByRoleName(String roleName);
}