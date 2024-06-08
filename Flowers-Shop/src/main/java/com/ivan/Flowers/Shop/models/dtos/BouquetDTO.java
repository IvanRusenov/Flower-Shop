package com.ivan.Flowers.Shop.models.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BouquetDTO(
        @Size(min = 2, max = 40, message = "Description must be between 2 and 40 symbols!")
        String description,
        @PositiveOrZero(message = "Number must be positive!")
        int itemNumber,
        @PositiveOrZero(message = "Price must be positive or zero")
        double price,
        @Size(min = 5, message = "Not valid url")
        String url
) {
        public static BouquetDTO empty() {
                return new BouquetDTO(null, 0, 0, null);
        }
}
