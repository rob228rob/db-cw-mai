package com.mai.db_cw_rbl.RolePackage.Dao;

import com.mai.db_cw_rbl.RolePackage.Role;
import com.mai.db_cw_rbl.UserPackage.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleDao {

    Optional<Role> findById(int id);

    Optional<Role> findByName(String roleUser);

    void save(Role newRole);

    List<Role> findRolesByUserId(UUID id);

    List<Role> findRolesByEmail(String email);

    void saveUserRoles(User user, List<Role> adminRole);
}
