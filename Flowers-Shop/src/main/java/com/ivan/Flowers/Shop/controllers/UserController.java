package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.models.dtos.UserLoginDTO;
import com.ivan.Flowers.Shop.models.dtos.UserRegisterDTO;
import com.ivan.Flowers.Shop.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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


    //TODO: implement delete user for admins
}
