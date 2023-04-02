package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.model.ExpenseModel;
import com.personalfinancemanager.domain.entity.ExpenseEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.repository.ExpenseRepository;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.util.mapper.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    public void addExpense(ExpenseModel model, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            ExpenseEntity expenseEntity = ExpenseMapper.modelToEntity(model);

            expenseEntity.setUser(userOptional.get());

            if (expenseEntity.getExpenseDate() == null) {
                expenseEntity.setExpenseDate(LocalDateTime.now());
            }

            expenseRepository.save(expenseEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find user when saving new expense!");
        }
    }

    public List<ExpenseModel> getExpensesForMonthAndYear(Integer year, Integer month, Integer userId) {
        List<ExpenseEntity> entities = expenseRepository.findAllByUserId(userId);

        List<ExpenseEntity> filteredEntities = entities.stream()
                .filter(x -> {
                    if (year == -1) {
                        return true;
                    }

                    if (month != 0 && month != -1) {
                        return x.getExpenseDate().getYear() == year && x.getExpenseDate().getMonthValue() == month;
                    }

                    return x.getExpenseDate().getYear() == year;
                })
                .collect(Collectors.toList());

        List<ExpenseModel> models = new ArrayList<>();
        for (ExpenseEntity entity : filteredEntities) {
            models.add(ExpenseMapper.entityToModel(entity));
        }

        return models;
    }

    public Set<Integer> getPossibleYears(Integer userId) {
        List<ExpenseEntity> entities = expenseRepository.findAllByUserId(userId);
        Set<Integer> possibleYears = new TreeSet<>();
        entities.forEach(x -> possibleYears.add(x.getExpenseDate().getYear()));

        return possibleYears;
    }
}
