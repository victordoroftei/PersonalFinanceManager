package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.dto.PercentageModel;
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
    public PercentageModel getStatistics(@RequestHeader("Authorization") String token) {
        return statisticsService.getStatistics(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
