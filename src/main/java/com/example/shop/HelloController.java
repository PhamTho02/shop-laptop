package com.example.shop;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Hello World from Spring";
    }

    // Add this method to test the role-based access control
    @GetMapping("/user")
    public String userPage() {
        return "Only user can see this page!";
    }

    // Add this method to test the role-based access control
    @GetMapping("/admin")
    public String adminPage() {
        return "Only users with role ADMIN can see this page!";
    }
}
