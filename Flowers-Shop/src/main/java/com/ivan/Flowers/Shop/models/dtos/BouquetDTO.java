package com.ivan.Flowers.Shop.models.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BouquetDTO(
        @Size(min = 2, max = 20, message = "Description must be between 2 and 20 symbols!")
        String description,
        @PositiveOrZero(message = "Number must be positive!")
        int number,
        @PositiveOrZero(message = "Price must be positive or zero")
        double price,
        @Size(min = 5, message = "Not valid url")
        String url
) {
}
