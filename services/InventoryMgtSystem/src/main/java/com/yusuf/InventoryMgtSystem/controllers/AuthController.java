package com.yusuf.InventoryMgtSystem.controllers;

import com.yusuf.InventoryMgtSystem.dtos.LoginRequest;
import com.yusuf.InventoryMgtSystem.dtos.RegisterRequest;
import com.yusuf.InventoryMgtSystem.dtos.Response;
import com.yusuf.InventoryMgtSystem.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

}
