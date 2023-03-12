package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.dto.ReceiptScannedModel;
import com.personalfinancemanager.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();

        try {
            String uploadedFilePath = System.getProperty("user.dir") + "\\uploads\\" + fileName;
            file.transferTo(new File(uploadedFilePath));

            testService.callPythonScript(uploadedFilePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public ReceiptScannedModel test() {
        return ReceiptScannedModel.builder()
                .detectedTotal(50.50)
                .build();
    }
}
