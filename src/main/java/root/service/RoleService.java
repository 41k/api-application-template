package root.service;

import root.model.Role;

import java.util.Collection;
import java.util.List;

public class RoleService {

    public Collection<Role> getRoles(String userId) {
        return List.of(Role.ROLE1);
    }
}
