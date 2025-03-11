package org.example.transactionservice.feign;

import org.example.transactionservice.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "http://localhost:8080", path = "/account")
public interface AccountClient {

    @GetMapping("/get/userId/{userId}")
    AccountDto getAccountByUserId(@PathVariable Long userId);

    @GetMapping("/get/username/{username}")
    AccountDto getAccountByUsername(@PathVariable String username);

    @GetMapping("/get/id/{id}")
    AccountDto getAccountById(@PathVariable Long id) ;

    @PostMapping("/deposit/{accountId}")
    void deposit(@PathVariable Long accountId, @RequestParam double amount);

    @PostMapping("/withdraw/{accountId}")
    void withdraw(@PathVariable Long accountId, @RequestParam double amount);
}
