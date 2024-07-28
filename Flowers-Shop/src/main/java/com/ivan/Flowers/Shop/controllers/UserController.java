package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.dtos.EditUserDTO;
import com.ivan.Flowers.Shop.models.dtos.UserLoginDTO;
import com.ivan.Flowers.Shop.models.dtos.UserRegisterDTO;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO) {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterDTO") @Valid UserRegisterDTO userRegisterDTO,
                                 BindingResult bindingResult) {

        ModelAndView register = new ModelAndView("register");

        if (bindingResult.hasErrors()){
            return register;
        }

        boolean isRegistered = userService.register(userRegisterDTO);

        if (isRegistered) {
            return new ModelAndView("redirect:/login");
        }

        return register;
    }


    @GetMapping("/login")
    public ModelAndView login(@ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO) {
        return new ModelAndView("login");
    }

//    @PostMapping("/login")
//    public ModelAndView login(@ModelAttribute("userLoginDTO") @Valid UserLoginDTO userLoginDTO,
//                              BindingResult bindingResult) {
//        ModelAndView login = new ModelAndView("login");
//
//        if (bindingResult.hasErrors()) {
//            return login;
//        }
//
//        boolean isLogged = userService.login(userLoginDTO);
//
//        if (!isLogged) {
//            return login;
//        }
//
//
//        boolean isModerator = userService.isMod(userLoginDTO);
//
//        if (isModerator) {
//            return new ModelAndView("redirect:/orders/pending");
//        }
//
//
//        return new ModelAndView("redirect:/home");
//
//    }


    @GetMapping("users/all")
    public ModelAndView getAllUsers() {

        ModelAndView users = new ModelAndView("users");

        users.addObject("users", userService.getAllUsers());

        return users;
    }

    @DeleteMapping("user/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return new ModelAndView("redirect:/users/all");
    }


    @GetMapping("/user/edit/{userId}")
    public ModelAndView editUser(@PathVariable("userId") Long userId) {
        ModelAndView editUser = new ModelAndView("edit-user");

        User user = userService.getUser(userId);
        EditUserDTO editUserDTO = modelMapper.map(user, EditUserDTO.class);
        editUserDTO.setRole(user.getRole().getType());
        //TODO: change User to have only one role

        editUser.addObject("roles", RoleType.values());
        editUser.addObject("editUserDTO", editUserDTO);

        return editUser;

    }


    @PostMapping("user/save")
    public ModelAndView save(@Valid EditUserDTO editUserDTO, BindingResult bindingResult) {


        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/user/edit/"+editUserDTO.getId());
        }

        userService.save(editUserDTO);

        return new ModelAndView("redirect:/users/all");
    }
}
