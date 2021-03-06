package com.example.codepile.web.controllers;

import com.example.codepile.error.ace.AceNotFoundException;
import com.example.codepile.error.authority.AuthorityNotFoundException;
import com.example.codepile.error.base.BaseException;
import com.example.codepile.error.pile.PileCannotBeEdited;
import com.example.codepile.error.pile.PileNotFoundException;
import com.example.codepile.error.user.EmailAlreadyExistsException;
import com.example.codepile.error.user.UserNotFoundException;
import com.example.codepile.error.user.UsernameAlreadyExistsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String EXCEPTION_VIEW_ROUTE = "/error";
    private static final String ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME = "statusCode";
    private static final String ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME = "message";

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            UsernameAlreadyExistsException.class})
    public ModelAndView handleAlreadyExistExceptions(BaseException exception){
        return this.fillModelAndView(exception.getCode(),exception.getMessage());
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            PileNotFoundException.class,
            AuthorityNotFoundException.class,
            AceNotFoundException.class})
    public ModelAndView handleNotFoundExceptions(BaseException exception){
        return this.fillModelAndView(exception.getCode(),exception.getMessage());
    }

    @ExceptionHandler({
            PileCannotBeEdited.class})
    public ModelAndView handleForbiddenExceptions(BaseException exception){
        return this.fillModelAndView(exception.getCode(),exception.getMessage());
    }

    private ModelAndView fillModelAndView(int statusCode, String message){
        ModelAndView modelAndView = new ModelAndView(EXCEPTION_VIEW_ROUTE);
        modelAndView.addObject(ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME,message);
        modelAndView.addObject(ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME, statusCode);
        return modelAndView;
    }
}
