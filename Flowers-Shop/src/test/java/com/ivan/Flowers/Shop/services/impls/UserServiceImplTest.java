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
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final String USERNAME = "Ivan";
    public static final String VALID_PASSWORD = "111";
    public static final String NOT_VALID_PASSWORD = "222";
    public static final String EMAIL = "ivan@email.bg";
    public static final String SHIPPING_ADDRESS = "Plovdiv";
    public static final String ENCODED_PASSWORD = "cvd8$sy";

    private UserServiceImpl toTest;

    @Mock
    private UserRepository mockedUserRepository;

    @Mock
    private RoleRepository mockedRoleRepository;

    @Mock
    private CartRepository mockedCartRepository;

    @Mock
    private PasswordEncoder mockedPasswordEncoder;

    @BeforeEach
    void setUp() {
        toTest = new UserServiceImpl(
                mockedUserRepository,
                mockedRoleRepository,
                mockedCartRepository,
                mockedPasswordEncoder
        );
    }

    @Test
    void register_UserAlreadyExists_ReturnsFalse() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername(USERNAME);

        when(mockedUserRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User()));

        boolean result = toTest.register(userRegisterDTO);

        assertFalse(result);
    }

    @Test
    void register_ValidUser_ReturnsTrue() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername(USERNAME);
        userRegisterDTO.setPassword(VALID_PASSWORD);
        userRegisterDTO.setConfirmPassword(VALID_PASSWORD);
        userRegisterDTO.setEmail(EMAIL);
        userRegisterDTO.setShippingAddress(SHIPPING_ADDRESS);

        when(mockedUserRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(mockedPasswordEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(mockedRoleRepository.findByType(RoleType.ROLE_USER)).thenReturn(new Role());
        when(mockedUserRepository.count()).thenReturn(1L);

        boolean result = toTest.register(userRegisterDTO);

        assertTrue(result);
        verify(mockedUserRepository, times(1))
                .saveAndFlush(any(User.class));

    }

    @Test
    void register_PasswordWrongConfirm_ReturnsFalse() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername(USERNAME);
        userRegisterDTO.setPassword(VALID_PASSWORD);
        userRegisterDTO.setConfirmPassword(NOT_VALID_PASSWORD);

        boolean result = toTest.register(userRegisterDTO);

        assertFalse(result);
    }

    @Test
    void register_FirstUser_ROLE_ADMIN() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername(USERNAME);
        userRegisterDTO.setPassword(VALID_PASSWORD);
        userRegisterDTO.setConfirmPassword(VALID_PASSWORD);
        userRegisterDTO.setEmail(EMAIL);
        userRegisterDTO.setShippingAddress(SHIPPING_ADDRESS);

        Role role = new Role();
        role.setType(RoleType.ROLE_ADMIN);

        when(mockedUserRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(mockedPasswordEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(mockedRoleRepository.findByType(RoleType.ROLE_USER)).thenReturn(new Role());
        when(mockedUserRepository.count()).thenReturn(0L);
        when(mockedRoleRepository.findByType(RoleType.ROLE_ADMIN)).thenReturn(role);

        toTest.register(userRegisterDTO);

        verify(mockedRoleRepository, times(1)).findByType(RoleType.ROLE_ADMIN);

    }

    @Test
    void login_InvalidCredentials_ReturnsFalse() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(USERNAME);
        userLoginDTO.setPassword(NOT_VALID_PASSWORD);

        when(mockedUserRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User()));

        boolean result = toTest.login(userLoginDTO);

        assertFalse(result);
    }

    @Test
    void login_ValidCredentials_ReturnsTrue() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(USERNAME);
        userLoginDTO.setPassword(VALID_PASSWORD);

        User user = new User();
        user.setPassword(ENCODED_PASSWORD);

        when(mockedUserRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(mockedPasswordEncoder.matches(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        boolean result = toTest.login(userLoginDTO);

        assertTrue(result);
    }

    @Test
    void getAllUsers_ReturnsUserList() {
        when(mockedUserRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = toTest.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void delete_UserExists_DeletesUser() {
        User user = new User();
        user.setId(1L);

        when(mockedUserRepository.findById(1L)).thenReturn(Optional.of(user));

        toTest.delete(1L);

        verify(mockedUserRepository).delete(user);
    }

    @Test
    void delete_UserDoesNotExist_ThrowsException() {
        when(mockedUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> toTest.delete(1L));
    }

    @Test
    void getUser_UserExists_ReturnsUser() {
        User user = new User();
        user.setId(1L);

        when(mockedUserRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = toTest.getUser(1L);

        assertEquals(user, foundUser);
    }

    @Test
    void getUser_UserDoesNotExist_ThrowsException() {
        when(mockedUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> toTest.getUser(1L));
    }

    @Test
    void save_UserDoesNotExist_ThrowsException() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setId(1L);

        when(mockedUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> toTest.save(editUserDTO));
    }
}
