package com.ivan.Flowers.Shop.inits;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RoleInit implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() > 0) {
            return;
        }

        List<Role> roles = new ArrayList<>();

        Arrays.stream(RoleType.values()).forEach(type -> {
            Role role = new Role();
            role.setType(type);
            roles.add(role);
        });

        roleRepository.saveAllAndFlush(roles);

    }
}
