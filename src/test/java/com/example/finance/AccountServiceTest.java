package com.example.finance;

import com.example.finance.model.Account;
import com.example.finance.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    void depositIncreasesBalance() {
        Account account = accountService.createAccount("Test User", BigDecimal.valueOf(100));
        accountService.deposit(account.getId(), BigDecimal.valueOf(50), "Salary");
        Account updated = accountService.getAccount(account.getId());
        // BigDecimal.equals() checks scale too (150 != 150.00), so use compareTo for value equality
        assertEquals(0, BigDecimal.valueOf(150).compareTo(updated.getBalance()));
    }

    @Test
    void withdrawDecreasesBalance() {
        Account account = accountService.createAccount("Test User", BigDecimal.valueOf(100));
        accountService.withdraw(account.getId(), BigDecimal.valueOf(30), "Rent");
        Account updated = accountService.getAccount(account.getId());
        assertEquals(0, BigDecimal.valueOf(70).compareTo(updated.getBalance()));
    }

    @Test
    void withdrawMoreThanBalanceThrows() {
        Account account = accountService.createAccount("Test User", BigDecimal.valueOf(20));
        assertThrows(IllegalStateException.class, () ->
                accountService.withdraw(account.getId(), BigDecimal.valueOf(50), "Too much"));
    }
}
