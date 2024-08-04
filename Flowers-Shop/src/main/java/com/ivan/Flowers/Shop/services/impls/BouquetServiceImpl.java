package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.CartItem;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartItemRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.services.BouquetService;
import com.ivan.Flowers.Shop.services.CloudinaryService;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//import java.util.logging.Logger;

@Service
public class BouquetServiceImpl implements BouquetService {

    private final CloudinaryService cloudinaryService;
    private final BouquetRepository bouquetRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public BouquetServiceImpl(
            CloudinaryService cloudinaryService,
            BouquetRepository bouquetRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository) {
        this.cloudinaryService = cloudinaryService;
        this.bouquetRepository = bouquetRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public boolean addBouquet(BouquetDTO bouquetDTO) throws IOException {

        Optional<Bouquet> optionalBouquet = bouquetRepository.findByItemNumber(bouquetDTO.getItemNumber());

        if (optionalBouquet.isPresent()) {
            return false;
        }

        List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif", "image/jpg", "image/webp");
        String fileContentType = bouquetDTO.getPicture().getContentType();

        if (!contentTypes.contains(fileContentType)) {
            return false;
        }

        Bouquet bouquet = new Bouquet();
        String url = cloudinaryService.uploadImage(bouquetDTO.getPicture());
        bouquet.setUrl(url);
        bouquet.setDescription(bouquetDTO.getDescription());
        bouquet.setItemNumber(bouquetDTO.getItemNumber());
        bouquet.setPrice(bouquetDTO.getUnitPrice());
        bouquet.setQuantity(bouquet.getQuantity() + 1);

        bouquetRepository.saveAndFlush(bouquet);

        return true;
    }

    @Override
    @Transactional
    public void removeBouquet(int itemNumber) throws IOException {
//        Logger logger = LoggerFactory.getLogger(getClass());

        Bouquet bouquet = bouquetRepository.findByItemNumber(itemNumber)
                .orElseThrow(() -> new ObjectNotFoundException("Bouquet not found", Bouquet.class.getSimpleName()));

        List<Cart> allCarts = cartRepository.findAll();

        allCarts.forEach(cart -> {

            CartItem itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getBouquet().getItemNumber() == itemNumber)
                    .findFirst()
                    .orElse(null);

            if (itemToRemove != null) {

                cart.removeCartItem(itemToRemove);
                cartItemRepository.delete(itemToRemove);
                cartRepository.save(cart);

            }

        });

        String url = bouquet.getUrl();

        bouquetRepository.delete(bouquet);
        boolean isDeleted = cloudinaryService.deleteImage(url);

        if (!isDeleted) {
//            logger.warn("Failed to delete image from cloudinary: {}", url);
            throw new RuntimeException("Failed to delete image from cloudinary!");
        }

//        logger.info("Bouquet {} removed successfully.", bouquet.getId());
    }

}
