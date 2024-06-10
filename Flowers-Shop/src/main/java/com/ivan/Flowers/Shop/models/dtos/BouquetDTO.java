package com.ivan.Flowers.Shop.models.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class BouquetDTO {

        @Size(min = 2, max = 40, message = "Description must be between 2 and 40 symbols!")
        String description;
        @PositiveOrZero(message = "Number must be positive!")
        int itemNumber;
        @PositiveOrZero(message = "Price must be positive or zero")
        double price;
        @Size(min = 5, message = "Not valid url")
        String url;

        MultipartFile picture;

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public int getItemNumber() {
                return itemNumber;
        }

        public void setItemNumber(int itemNumber) {
                this.itemNumber = itemNumber;
        }

        public double getPrice() {
                return price;
        }

        public void setPrice(double price) {
                this.price = price;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public MultipartFile getPicture() {
                return picture;
        }

        public void setPicture(MultipartFile picture) {
                this.picture = picture;
        }
}
