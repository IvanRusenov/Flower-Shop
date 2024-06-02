package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.UserRegisterDTO;

public interface UserService {
    boolean register(UserRegisterDTO userRegisterDTO);
}
