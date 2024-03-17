package com.fil.rouge.service;
import com.fil.rouge.domain.Role;
import com.fil.rouge.utils.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoleService {
    Role save(Role role) throws ValidationException;
    Optional<Role> findByName(String name) ;
    List<Role> getALlRoles();
    void delete(Long id);

    Role findById(Long id);

    List<Role> findAllByNameIn(List<String> roleNames);
}
