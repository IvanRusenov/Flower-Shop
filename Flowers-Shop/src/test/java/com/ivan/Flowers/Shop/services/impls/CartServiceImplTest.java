package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.CartItem;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartItemRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    private static final String USERNAME = "Ivan";
    private static final String PASSWORD = "111";
    private static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    private static final int ITEM_NUMBER = 123;
    private static final double UNIT_PRICE = 2365.25;

    @Mock
    private UserRepository mockedUserRepository;

    @Mock
    private BouquetRepository mockedBouquetRepository;

    @Mock
    private CartItemRepository mockedCartItemRepository;

    @Mock
    private CartRepository mockedCartRepository;

    @InjectMocks
    private CartServiceImpl toTest;

    private UserDetails userDetails;
    private User user;
    private Cart cart;
    private Bouquet bouquet;

    @BeforeEach
    void setUp() {

        userDetails = new ShopUserDetails(
                USERNAME,
                PASSWORD,
                List.of(ROLE_USER));

        user = new User();
        user.setUsername(USERNAME);
        cart = new Cart();
        user.setCart(cart);
        bouquet = new Bouquet();
        bouquet.setId(1);
        bouquet.setItemNumber(ITEM_NUMBER);
        bouquet.setPrice(UNIT_PRICE);
    }

    @Test
    void testAddToCart_itemNotInCart() {

        when(mockedUserRepository.findByUsername(userDetails.getUsername()))
                .thenReturn(Optional.of(user));
        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER)).thenReturn(Optional.of(bouquet));

        toTest.addToCart(ITEM_NUMBER, userDetails);

        assertInstanceOf(ShopUserDetails.class, userDetails);
        assertEquals(1, cart.getItems().size());

        CartItem cartItem = cart.getItems().getFirst();

        assertEquals(1, cartItem.getQuantity());
        assertEquals(UNIT_PRICE, cartItem.getUnitPrice());
        assertEquals(bouquet, cartItem.getBouquet());
        assertEquals(ITEM_NUMBER, cartItem.getBouquet().getItemNumber());
        assertEquals(1, cartItem.getBouquet().getId());

        verify(mockedCartItemRepository, times(1)).save(cartItem);
        verify(mockedCartRepository, times(1)).save(cart);

    }

    @Test
    void testAddToCart_ItemIsInCart() {
        when(mockedUserRepository.findByUsername(userDetails.getUsername()))
                .thenReturn(Optional.of(user));
        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER)).thenReturn(Optional.of(bouquet));

        toTest.addToCart(ITEM_NUMBER, userDetails);

        CartItem cartItem = cart.getItems().getFirst();

        toTest.addToCart(ITEM_NUMBER, userDetails);

        verify(mockedCartItemRepository, times(1)).save(cartItem);
        assertEquals(2, cartItem.getQuantity());
        assertEquals(UNIT_PRICE, cartItem.getUnitPrice());

        double totalPrice = user.getCart().getItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getUnitPrice())
                .sum();

        assertEquals(UNIT_PRICE * 2, totalPrice);
        verify(mockedCartItemRepository, times(1)).save(cartItem);
        verify(mockedCartRepository, times(2)).save(cart);

    }

    @Test
    void testAddToCart_UserNotAuthenticated_Throws() {
        UserDetails invalidUserDetails = mock(UserDetails.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> toTest.addToCart(ITEM_NUMBER, invalidUserDetails));
        assertEquals("User is not authenticated.", runtimeException.getMessage());
    }

    @Test
    void getCart() {

        when(mockedUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(user));

        Cart resultCart = toTest.getCart(userDetails);

        assertEquals(cart, resultCart);
        assertEquals(cart.getItems().size(), resultCart.getItems().size());

    }

    @Test
    void testGetCart_UserNotAuthenticated_Throws() {
        UserDetails invalidUserDetails = mock(UserDetails.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> toTest.getCart(invalidUserDetails));
        assertEquals("User is not authenticated.", runtimeException.getMessage());
    }

    @Test
    void clearCart_clearsCart() {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cart.addCartItem(cartItem);
        when(mockedCartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        toTest.clearCart(1L);

        assertTrue(cart.getItems().isEmpty());
        verify(mockedCartRepository, times(1)).save(cart);
    }

    @Test
    void getItems_returnsItems() {

        CartItem cartItem = new CartItem();
        cart.addCartItem(cartItem);
        when(mockedUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(user));

        List<CartItem> items = toTest.getItems(userDetails);

        assertEquals(1, items.size());
        assertEquals(cartItem, items.getFirst());
    }

    @Test
    void testGetItems_UserNotAuthenticated_Throws() {
        UserDetails invalidUserDetails = mock(UserDetails.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> toTest.getItems(invalidUserDetails));
        assertEquals("User is not authenticated.", runtimeException.getMessage());
    }

    @Test
    void testRemoveCartItem_removesItem() {

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cart.getItems().add(cartItem);

        when(mockedUserRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(mockedCartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(mockedCartRepository.findById(user.getCart().getId())).thenReturn(Optional.of(cart));

        toTest.remove(1L, userDetails);

        assertTrue(cart.getItems().isEmpty());
        verify(mockedCartRepository, times(1)).saveAndFlush(cart);
        verify(mockedCartItemRepository, times(1)).delete(cartItem);
        verify(mockedUserRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void testRemove_UserNotAuthenticated_Throws() {
        UserDetails invalidUserDetails = mock(UserDetails.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> toTest.remove(1L, invalidUserDetails));
        assertEquals("User is not authenticated.", runtimeException.getMessage());
    }

}
