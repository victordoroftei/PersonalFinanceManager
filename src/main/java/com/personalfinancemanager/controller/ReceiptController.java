package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.model.ReceiptModel;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ReceiptModel upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        return receiptService.handleFile(file, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReceipt(@RequestBody ReceiptModel model, @RequestHeader("Authorization") String token) {
        receiptService.addReceipt(model, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public List<ReceiptModel> getReceiptsForMonthAndYear(@RequestParam Integer year, @RequestParam Integer month, @RequestHeader("Authorization") String token) {
        return receiptService.getReceiptsForMonthAndYear(year, month, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/years")
    @ResponseStatus(HttpStatus.OK)
    public Set<Integer> getPossibleReceiptYears(@RequestHeader("Authorization") String token) {
        return receiptService.getPossibleReceiptYears(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
