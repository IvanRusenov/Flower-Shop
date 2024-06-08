package com.ivan.Flowers.Shop.inits;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Initialisations implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final BouquetRepository bouquetRepository;

    public Initialisations(RoleRepository roleRepository, BouquetRepository bouquetRepository) {
        this.roleRepository = roleRepository;
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        initRoles();
        initBouquets();

    }

    private void initBouquets() {

        if (bouquetRepository.count() > 0) {
            return;
        }

        Bouquet bouquet = new Bouquet();
        bouquet.setDescription("Bouquet of light pink roses in a box");
        bouquet.setPrice(362.5);
        bouquet.setQuantity(4);
        bouquet.setUrl("/img/boxes/box1.jpg");
        bouquet.setItemNumber(264);

        bouquetRepository.saveAndFlush(bouquet);

    }

    private void initRoles() {

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
