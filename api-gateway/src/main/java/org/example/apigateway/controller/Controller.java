package org.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping
    public String hello() {
        return """
                
                Hello
                
                http://localhost:8080/auth
                
                http://localhost:8080/user
                
                http://localhost:8080/transaction
                
                http://localhost:8080/account
                
                
                """;
    }
}
