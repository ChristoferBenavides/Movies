package com.christoferB.movies.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public String accessDenied() {
        return "403"; // Thymeleaf template name without the .html extension
    }
}
