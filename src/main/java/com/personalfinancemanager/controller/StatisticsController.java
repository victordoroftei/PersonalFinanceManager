package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.model.ExpensesMonthlyStatisticsModel;
import com.personalfinancemanager.domain.model.PercentageModel;
import com.personalfinancemanager.domain.model.YearlyStatisticsModel;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PercentageModel getStatistics(@RequestParam Integer year, @RequestParam Integer month, @RequestHeader("Authorization") String token) {
        return statisticsService.getStatistics(year, month, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/yearly")
    @ResponseStatus(HttpStatus.OK)
    public YearlyStatisticsModel getYearlyStatisticsByMonth(@RequestParam Integer year, @RequestHeader("Authorization") String token) {
        return statisticsService.getYearlyStatisticsByMonth(year, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/expenses")
    @ResponseStatus(HttpStatus.OK)
    public ExpensesMonthlyStatisticsModel getExpensesMonthly(@RequestParam Integer year, @RequestParam Integer month, @RequestHeader("Authorization") String token) {
        return statisticsService.getExpensesGroupedByType(year, month, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
