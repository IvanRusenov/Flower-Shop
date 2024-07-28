package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Role;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceImplTest {

    private final static String USER_ROLE_TYPE = "ROLE_USER";
    private final static String USERNAME = "Ivan";
    private final static String NOT_EXISTING_USERNAME = "Onofri";
    private final static String PASSWORD = "111";

    private UserDetailServiceImpl toTest;

    @Mock
    private UserRepository mockedUserRepository;

    @BeforeEach
    void setUp() {

        toTest = new UserDetailServiceImpl(mockedUserRepository);

    }

    @Test
    void testLoadUserByUsername() {

        Role testRole = new Role();
        testRole.setType(RoleType.ROLE_USER);
        testRole.setId(1);

        User testUser = new User();
        testUser.setUsername(USERNAME);
        testUser.setPassword(PASSWORD);
        testUser.setRole(testRole);

        when(mockedUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = toTest.loadUserByUsername(USERNAME);

        Assertions.assertInstanceOf(ShopUserDetails.class, userDetails);

        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> toTest.loadUserByUsername(NOT_EXISTING_USERNAME));

        Assertions.assertEquals(USERNAME, userDetails.getUsername());
        Assertions.assertEquals(PASSWORD, userDetails.getPassword());
        Assertions.assertEquals(1, userDetails.getAuthorities().size());

        String actualAuthorityType = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        Assertions.assertEquals(USER_ROLE_TYPE, actualAuthorityType);

    }

}
