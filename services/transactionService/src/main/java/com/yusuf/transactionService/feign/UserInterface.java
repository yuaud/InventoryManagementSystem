package com.yusuf.transactionService.feign;

import com.yusuf.transactionService.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

//INVENTORYMGTSYSTEM
@FeignClient(value = "inventoryMgtSystem", configuration = FeignClientConfig.class)
public interface UserInterface {

    @GetMapping("/api/users/feign/getCurrentUser")
    public ResponseEntity<User> getCurrentUser();
}
