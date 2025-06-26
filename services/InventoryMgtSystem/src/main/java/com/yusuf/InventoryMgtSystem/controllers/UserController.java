package com.yusuf.InventoryMgtSystem.controllers;

import com.yusuf.InventoryMgtSystem.dtos.LoginRequest;
import com.yusuf.InventoryMgtSystem.dtos.RegisterRequest;
import com.yusuf.InventoryMgtSystem.dtos.Response;
import com.yusuf.InventoryMgtSystem.dtos.UserDTO;
import com.yusuf.InventoryMgtSystem.models.User;
import com.yusuf.InventoryMgtSystem.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<Response> getUserTransactions(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserTransactions(userId));
    }

    @GetMapping("/feign/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }

}
