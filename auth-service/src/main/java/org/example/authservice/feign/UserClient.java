package org.example.authservice.feign;

import org.example.authservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/user/add")
    void addUser(@RequestBody User userDto);

    @GetMapping("/user/getByEmail")
    User findByEmail(@RequestParam String email);

    @GetMapping("/user/getByUsername")
    User findByUsername(@RequestParam String username);
}
