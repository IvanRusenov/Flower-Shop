package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException onfe) {

        ModelAndView modelAndView = new ModelAndView("object-not-found");

        modelAndView.addObject("objectName", onfe.getObjectName());

        return modelAndView;
    }

}
