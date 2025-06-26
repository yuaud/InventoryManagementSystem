package com.yusuf.transactionService.services;

import com.yusuf.transactionService.dtos.LoginRequest;
import com.yusuf.transactionService.dtos.RegisterRequest;
import com.yusuf.transactionService.dtos.Response;
import com.yusuf.transactionService.dtos.UserDTO;
import com.yusuf.transactionService.models.User;

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
