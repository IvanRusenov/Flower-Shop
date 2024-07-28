package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.RoleRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    private static final String EMAIL = "ivan@email.bg";
    private static final String USERNAME = "ivan";
    private static final String SHIPPING_ADDRESS = "c. Plovdiv";
    private final static String PASSWORD = "111";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setUsername(USERNAME);
        user.setShippingAddress(SHIPPING_ADDRESS);
        user.setPassword(passwordEncoder.encode(PASSWORD));

        Cart cart = new Cart();
        user.setCart(cart);

        Role role = roleRepository.findByType(RoleType.ROLE_MODERATOR);
        user.setRole(role);

        cartRepository.save(cart);
        return userRepository.save(user);
    }

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testRegisterGet() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    void testRegister() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", EMAIL)
                        .param("username", USERNAME)
                        .param("shippingAddress", SHIPPING_ADDRESS)
                        .param("password", PASSWORD)
                        .param("confirmPassword", PASSWORD)
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Optional<User> byUsername = userRepository.findByUsername(USERNAME);

        Assertions.assertTrue(byUsername.isPresent());

        User user = byUsername.get();

        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(SHIPPING_ADDRESS, user.getShippingAddress());
        Assertions.assertTrue(passwordEncoder.matches(PASSWORD, user.getPassword()));
    }

    @Test
    void testRegisterWithErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "")
                        .param("username", "")
                        .param("shippingAddress", "")
                        .param("password", "")
                        .param("confirmPassword", "")
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "email", "username", "shippingAddress", "password", "confirmPassword"));
    }

    @Test
    void testRegisterInvalidConfirm() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", EMAIL)
                        .param("username", USERNAME)
                        .param("shippingAddress", SHIPPING_ADDRESS)
                        .param("password", PASSWORD)
                        .param("confirmPassword", "222")
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(view().name("register"));

    }

    @Test
    void testLoginGet() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("userLoginDTO"));
    }

    @Test
    void testLogin() throws Exception {
        createUser();

        mockMvc.perform(post("/login")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/pending"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        createUser();

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        User user = createUser();

        mockMvc.perform(delete("/user/delete/" + user.getId())
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));

        Optional<User> deletedUser = userRepository.findById(user.getId());
        Assertions.assertTrue(deletedUser.isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditUserGet() throws Exception {
        User user = createUser();

        mockMvc.perform(get("/user/edit/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("editUserDTO"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveUser() throws Exception {
        User user = createUser();

        mockMvc.perform(post("/user/save")
                        .param("id", String.valueOf(user.getId()))
                        .param("username", "ivan_new")
                        .param("email", "ivan_new@email.bg")
                        .param("shippingAddress", "c. Sofia")
                        .param("role", "ROLE_MODERATOR")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));

        Optional<User> updatedUser = userRepository.findByUsername("ivan_new");
        Assertions.assertTrue(updatedUser.isPresent());
        User userFromDb = updatedUser.get();

        Assertions.assertEquals("ivan_new", userFromDb.getUsername());
        Assertions.assertEquals("ivan_new@email.bg", userFromDb.getEmail());
        Assertions.assertEquals("c. Sofia", userFromDb.getShippingAddress());
        Assertions.assertEquals(RoleType.ROLE_MODERATOR, userFromDb.getRole().getType());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveUserWithInvalidData() throws Exception {

        User user = createUser();

        mockMvc.perform(post("/user/save")
                        .param("id", "2222222") // Invalid ID
                        .param("username", "ivan_new")
                        .param("email", EMAIL)
                        .param("shippingAddress", SHIPPING_ADDRESS)
                        .param("role", "ROLE_MODERATOR")
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assertions.assertInstanceOf(ObjectNotFoundException.class,
                        result.getResolvedException()));

        Optional<User> updatedUser = userRepository.findByUsername("ivan_new");
        Assertions.assertFalse(updatedUser.isPresent());

    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveUserWithErrors() throws Exception {
        User user = createUser();

        mockMvc.perform(post("/user/save")
                        .param("id", String.valueOf(user.getId()))
                        .param("username", "")
                        .param("email", "")
                        .param("shippingAddress", "")
                        .param("role", "")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/edit/" + user.getId()));
    }
}
