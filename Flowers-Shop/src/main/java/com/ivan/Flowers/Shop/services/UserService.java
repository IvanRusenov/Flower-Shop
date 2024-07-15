package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.UserLoginDTO;
import com.ivan.Flowers.Shop.models.dtos.UserRegisterDTO;
import com.ivan.Flowers.Shop.models.entities.User;

import java.util.List;

public interface UserService {
    boolean register(UserRegisterDTO userRegisterDTO);

    boolean login(UserLoginDTO userLoginDTO);

    boolean isMod(UserLoginDTO userLoginDTO);

    List<User> getAllUsers();

    void delete(long id);
}
