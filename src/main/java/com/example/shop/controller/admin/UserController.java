package com.example.shop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.shop.domain.User;
import com.example.shop.service.UploadService;
import com.example.shop.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String postCreateUser(Model model, @ModelAttribute("newUser") @Valid User newUser,
            BindingResult newUserBindingResult,
            @RequestParam("nameAvatarFile") MultipartFile file) {

        // validate

        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        String avatar = this.uploadService.handleSaveUploadFile(file, "admin/images/avatar");
        String hashPassword = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setAvatar(avatar);
        newUser.setPassword(hashPassword);

        newUser.setRole(this.userService.getRoleByName(newUser.getRole().getName()));

        userService.handleSaveUser(newUser);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin/user/show";
    }

    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "/admin/user/detail";
    }

    @GetMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        model.addAttribute("updateUser", userService.getUserById(id));
        return "/admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("updateUser") @Valid User updateUser,
            BindingResult updateUserBindingResult) {
        if (updateUserBindingResult.hasErrors()) {
            return "admin/user/update";
        }
        updateUser.setAvatar(updateUser.getAvatar());
        updateUser.setPassword(updateUser.getPassword());
        updateUser.setRole(this.userService.getRoleByName(updateUser.getRole().getName()));

        userService.handleSaveUser(updateUser);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("deleteUser", userService.getUserById(id));
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("deleteUser") User deleteUser) {
        this.userService.handleDeleteUser(deleteUser.getId());
        return "redirect:/admin/user";
    }

}
