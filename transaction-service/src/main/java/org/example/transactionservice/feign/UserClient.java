package org.example.transactionservice.feign;

import org.example.transactionservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/user/getByEmail")
    UserDto findByEmail(@RequestParam String email);

    @GetMapping("/user/getByUsername")
    UserDto findByUsername(@RequestParam String username);

    @GetMapping("/user/getById")
    UserDto findById(@RequestParam Long id);
}