package com.ivan.Flowers.Shop.inits;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.RoleRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class Initialisations implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final BouquetRepository bouquetRepository;
    private final UserRepository userRepository;

    public Initialisations(RoleRepository roleRepository, BouquetRepository bouquetRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.bouquetRepository = bouquetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        initRoles();
//        initBouquets();

//        initModerator();

    }

//    private void initModerator() {
//
//        Optional<User> byUsername = userRepository.findByUsername("mod");
//
//        if (byUsername.isEmpty()) {
//            return;
//        }
//
////        Role role = roleRepository.findById(2L).get();
//        Role role = roleRepository.findByType(RoleType.ROLE_MODERATOR);
//
//        if (byUsername.get().getRoles().contains(role)) {
//            return;
//        }
//
//        byUsername.get().getRoles().clear();
//        byUsername.get().getRoles().add(role);
//
//        userRepository.save(byUsername.get());
//    }


    private void initBouquets() {

        if (bouquetRepository.count() > 0) {
            return;
        }

        Bouquet bouquet = new Bouquet();
        bouquet.setDescription("Bouquet of light pink roses in a box");
        bouquet.setPrice(362.5);
        bouquet.setQuantity(4);
        bouquet.setUrl("/uploads/box1.jpg");
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
