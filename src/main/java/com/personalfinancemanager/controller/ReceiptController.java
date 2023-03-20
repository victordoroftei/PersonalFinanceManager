package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.dto.ReceiptModel;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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
    public void addReceipt(@RequestBody ReceiptModel data, @RequestHeader("Authorization") String token) {
        receiptService.addReceipt(data, JWTAuthorizationFilter.getUserIdFromJwt(token));
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

    @GetMapping(value = "/image/{imagePath}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageWithMediaType(@PathVariable String imagePath) throws IOException {
        FileInputStream in = new FileInputStream("E:\\__Teme\\_Licenta\\PersonalFinanceManager\\uploads\\" + imagePath);
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ReceiptModel getExtractedDataTest() {
        return ReceiptModel.builder()
                .calculatedTotal(55.16)
                .detectedTotal(55.26)
                .receiptDate(LocalDateTime.parse("2022-12-02T10:53:24"))
                .imagePath("E:\\__Teme\\_Licenta\\PersonalFinanceManager\\uploads\\7c1f88d1-f952-4b84-8ab9-aed56c138cb7.jpg")
                .itemNames(List.of("STRUGURI. ROZ", "MARLBORO RED KS"))
                .itemPrices(List.of(8.16, 47.0))
                .retailer("PROFI")
                .build();
    }

    @GetMapping("/one-entity")
    @ResponseStatus(HttpStatus.OK)
    public ReceiptEntity getReceiptEntity() {
        return receiptService.getOne();
    }

}
