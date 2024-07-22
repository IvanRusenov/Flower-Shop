package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.dtos.EditUserDTO;
import com.ivan.Flowers.Shop.models.dtos.UserLoginDTO;
import com.ivan.Flowers.Shop.models.dtos.UserRegisterDTO;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.RoleRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.services.UserService;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CartRepository cartRepository,
                           PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {

        Optional<User> optionalUser = userRepository.findByUsername(userRegisterDTO.getUsername());

        if (optionalUser.isPresent()) {
            return false;
        }

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return false;
        }

        User user = new User();
//        user.setOrders(new ArrayList<>());
        user.setRoles(new ArrayList<>());

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        cartRepository.saveAndFlush(cart);
        user.setCart(cart);

        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setShippingAddress(userRegisterDTO.getShippingAddress());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        Role role = roleRepository.findByType(RoleType.ROLE_USER);

        if (userRepository.count() == 0) {

            user.getRoles().add(roleRepository.findByType(RoleType.ROLE_ADMIN));

        } else {

            user.getRoles().add(role);
        }


        userRepository.saveAndFlush(user);

        return true;
    }

    @Override
    public boolean login(UserLoginDTO userLoginDTO) {

        Optional<User> optionalUser = userRepository.findByUsername(userLoginDTO.getUsername());

        return optionalUser
                .filter(user -> passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword()))
                .isPresent();

    }

    @Override
    public boolean isMod(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername()).orElseThrow();
        Role role = roleRepository.findByType(RoleType.ROLE_MODERATOR);
        return user.getRoles().contains(role);
    }

    @Override
    public List<User> getAllUsers() {

        //TODO: return userInfoDTO ?
        return userRepository.findAll();
    }

    @Override
    public void delete(long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found", User.class.getSimpleName()));
        userRepository.delete(user);

    }

    @Override
    public User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User not found", User.class.getSimpleName()));
    }

    @Override
    public boolean save(EditUserDTO editUserDTO) {

        User user = userRepository.findById(editUserDTO.getId()).orElseThrow(
                () -> new ObjectNotFoundException("User not found", User.class.getSimpleName())
        );

        user.setEmail(editUserDTO.getEmail());
        user.setShippingAddress(editUserDTO.getShippingAddress());
        user.setUsername(editUserDTO.getUsername());
        Role role = roleRepository.findByType(editUserDTO.getRole());
        user.setRoles(List.of(role));

        userRepository.save(user);

        return true;

    }
}
