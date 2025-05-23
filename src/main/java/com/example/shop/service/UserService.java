package com.example.shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.shop.domain.Role;
import com.example.shop.domain.User;
import com.example.shop.domain.dto.RegisterDTO;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;

@Service
public class UserService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }

    public Page<User> fetchUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public User registerDTOtoUser(RegisterDTO registerDOT) {
        User user = new User();

        user.setFullName(registerDOT.getFirstName() + " " + registerDOT.getLastName());
        user.setEmail(registerDOT.getEmail());
        user.setPassword(registerDOT.getPassword());

        return user;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public long countUsers() {
        return this.userRepository.count();
    }

    public long countProducts() {
        return this.productRepository.count();
    }
}
