package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.CartItem;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartItemRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.services.CloudinaryService;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BouquetServiceImplTest {

    private final static String DESCRIPTION = "Bouquet of flowers";
    private final static String URL = "http://res.cloudinary.com/c9b8ifuk2jvtckawsefc.png";
    private final static int ITEM_NUMBER = 987;
    private final static int ITEM_NUMBER_DIFFERENT = 789;
    private final static double UNIT_PRICE = 150.25;

    private BouquetServiceImpl toTest;

    @Mock
    private CloudinaryService mockedCloudinaryService;
    @Mock
    private BouquetRepository mockedBouquetRepository;
    @Mock
    private CartRepository mockedCartRepository;
    @Mock
    private CartItemRepository mockedCartItemRepository;
    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {

        toTest = new BouquetServiceImpl(
                mockedCloudinaryService,
                mockedBouquetRepository,
                mockedCartRepository,
                mockedCartItemRepository
        );

    }

    @Test
    void testAddBouquet() throws IOException {

        BouquetDTO testDto = new BouquetDTO();
        testDto.setItemNumber(ITEM_NUMBER);
        testDto.setPicture(mockFile);

        Bouquet testBouquet = new Bouquet();

        when(mockedCloudinaryService.uploadImage(testDto.getPicture()))
                .thenReturn(URL);

        when(mockFile.getContentType()).thenReturn("image/jpg");
        Assertions.assertTrue(toTest.addBouquet(testDto));

        when(mockFile.getContentType()).thenReturn("application/pdf");
        Assertions.assertFalse(toTest.addBouquet(testDto));

        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER)).thenReturn(Optional.of(testBouquet));
        Assertions.assertFalse(toTest.addBouquet(testDto));

    }

    @Test
    void testRemoveBouquetRemove() throws IOException {

        Bouquet bouquet = new Bouquet();
        bouquet.setQuantity(10);
        bouquet.setUrl(URL);
        bouquet.setPrice(UNIT_PRICE);
        bouquet.setDescription(DESCRIPTION);
        bouquet.setItemNumber(ITEM_NUMBER);
        bouquet.setId(1);

        CartItem cartItem = new CartItem();
        cartItem.setBouquet(bouquet);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(20.50);
        cartItem.setId(2);

        Cart cart = new Cart();
        cart.addCartItem(cartItem);

        Cart cart1 = new Cart();

        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        carts.add(cart1);

        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER))
                .thenReturn(Optional.of(bouquet));
        when(mockedCartRepository.findAll()).thenReturn(carts);
        when(mockedCloudinaryService.deleteImage(URL)).thenReturn(true);

        toTest.removeBouquet(ITEM_NUMBER);

        verify(mockedCartItemRepository, times(1)).delete(cartItem);
        verify(mockedCartRepository, times(1)).save(cart);
        verify(mockedBouquetRepository, times(1)).delete(bouquet);
        Assertions.assertEquals(0, cart.getItems().size());

    }

    @Test
    void testRemoveBouquetWithNoExistingItemNumber() {

        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> toTest.removeBouquet(ITEM_NUMBER));

    }

    @Test
    void testRemoveBouquetWithCloudinaryFalse() throws IOException {

        Bouquet bouquet = new Bouquet();
        bouquet.setQuantity(10);
        bouquet.setUrl(URL);
        bouquet.setPrice(UNIT_PRICE);
        bouquet.setDescription(DESCRIPTION);
        bouquet.setItemNumber(ITEM_NUMBER);
        bouquet.setId(1);

        CartItem cartItem = new CartItem();
        cartItem.setBouquet(bouquet);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(20.50);
        cartItem.setId(2);

        Cart cart = new Cart();
        cart.addCartItem(cartItem);

        Cart cart1 = new Cart();

        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        carts.add(cart1);

        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER))
                .thenReturn(Optional.of(bouquet));
        when(mockedCartRepository.findAll()).thenReturn(carts);
        when(mockedCloudinaryService.deleteImage(URL)).thenReturn(false);

        Assertions.assertThrows(RuntimeException.class,
                () -> toTest.removeBouquet(ITEM_NUMBER));

    }

    @Test
    void testRemoveBouquetWithEmptyCart() throws IOException {

        Bouquet bouquet = new Bouquet();
        bouquet.setQuantity(10);
        bouquet.setUrl(URL);
        bouquet.setPrice(UNIT_PRICE);
        bouquet.setDescription(DESCRIPTION);
        bouquet.setItemNumber(ITEM_NUMBER);
        bouquet.setId(1);

        Bouquet bouquet1 = new Bouquet();
        bouquet1.setQuantity(10);
        bouquet1.setUrl(URL);
        bouquet1.setPrice(UNIT_PRICE);
        bouquet1.setDescription(DESCRIPTION);
        bouquet1.setItemNumber(ITEM_NUMBER_DIFFERENT);
        bouquet1.setId(2);

        CartItem cartItem = new CartItem();
        cartItem.setBouquet(bouquet1);
        Cart cart = new Cart();
        cart.addCartItem(cartItem);
        List<Cart> carts = List.of(cart);

        when(mockedBouquetRepository.findByItemNumber(ITEM_NUMBER))
                .thenReturn(Optional.of(bouquet));
        when(mockedCartRepository.findAll()).thenReturn(carts);
        when(mockedCloudinaryService.deleteImage(URL)).thenReturn(true);

        toTest.removeBouquet(ITEM_NUMBER);

        verify(mockedCartItemRepository, times(0)).delete(cartItem);
        verify(mockedCartRepository, times(0)).save(cart);

    }

}
