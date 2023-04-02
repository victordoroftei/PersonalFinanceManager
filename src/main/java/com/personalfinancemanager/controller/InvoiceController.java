package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.dto.InvoiceModel;
import com.personalfinancemanager.domain.entity.InvoiceEntity;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    /*

    @GetMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceModel getInvoiceById(@PathVariable String invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

     */

    @GetMapping("/years")
    @ResponseStatus(HttpStatus.OK)
    public Set<Integer> getPossibleInvoiceYears(@RequestHeader("Authorization") String token) {
        return invoiceService.getPossibleInvoiceYears(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceModel> getInvoicesForMonthAndYear(@RequestParam Integer year, @RequestParam Integer month, @RequestHeader("Authorization") String token) {
        return invoiceService.getReceiptsForMonthAndYear(year, month, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/due")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceEntity> getDueInvoices(@RequestHeader("Authorization") String token) {
        return invoiceService.getDueInvoices(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void payInvoice(@RequestBody InvoiceEntity invoice) {
        invoiceService.payInvoice(invoice);
    }
}
