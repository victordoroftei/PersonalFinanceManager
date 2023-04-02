package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.model.ExpenseModel;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addExpense(@RequestBody ExpenseModel model, @RequestHeader("Authorization") String token) {
        expenseService.addExpense(model, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/years")
    @ResponseStatus(HttpStatus.OK)
    public Set<Integer> getPossibleYears(@RequestHeader("Authorization") String token) {
        return expenseService.getPossibleYears(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
