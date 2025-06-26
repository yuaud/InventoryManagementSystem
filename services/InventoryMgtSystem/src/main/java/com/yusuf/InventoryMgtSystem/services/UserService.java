package com.yusuf.InventoryMgtSystem.services;

import com.yusuf.InventoryMgtSystem.dtos.LoginRequest;
import com.yusuf.InventoryMgtSystem.dtos.RegisterRequest;
import com.yusuf.InventoryMgtSystem.dtos.Response;
import com.yusuf.InventoryMgtSystem.dtos.UserDTO;
import com.yusuf.InventoryMgtSystem.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
