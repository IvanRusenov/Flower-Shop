package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.UserLoginDTO;
import com.ivan.Flowers.Shop.models.dtos.UserRegisterDTO;

public interface UserService {
    boolean register(UserRegisterDTO userRegisterDTO);

    boolean login(UserLoginDTO userLoginDTO);

    boolean isMod(UserLoginDTO userLoginDTO);
}
