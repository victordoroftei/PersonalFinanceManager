package com.personalfinancemanager.controller.advice;

import com.personalfinancemanager.domain.dto.InvoiceModel;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addInvoice(@RequestBody InvoiceModel model, @RequestHeader("Authorization") String token) {
        invoiceService.addInvoice(model, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceModel getInvoiceById(@PathVariable String invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }
}
