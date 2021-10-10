package com.ttk.developer.recon.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ReconExceptionHandler extends ResponseEntityExceptionHandler {

    private boolean status;
    private String message;

    //public String

}
