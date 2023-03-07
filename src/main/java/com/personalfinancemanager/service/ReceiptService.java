package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.dto.ReceiptModel;
import com.personalfinancemanager.domain.dto.ReceiptScannedDto;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.domain.entity.ReceiptItemEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.domain.repository.ReceiptItemRepository;
import com.personalfinancemanager.domain.repository.ReceiptRepository;
import com.personalfinancemanager.domain.repository.UserRepository;
import com.personalfinancemanager.exception.FileProcessException;
import com.personalfinancemanager.exception.FileSaveException;
import com.personalfinancemanager.exception.ScriptException;
import com.personalfinancemanager.util.JsonUtil;
import com.personalfinancemanager.util.ReceiptMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptItemRepository receiptItemRepository;

    private final UserRepository userRepository;

    public void addReceipt(ReceiptModel data, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            ReceiptEntity receiptEntity = ReceiptMapper.modelToEntity(data);
            receiptEntity.setUser(userOptional.get());
            receiptEntity.setInsertedDate(LocalDateTime.now());

            receiptRepository.save(receiptEntity);
            saveReceiptItems(data, receiptEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find user when saving extracted receipt data!");
        }
    }

    public ReceiptModel handleFile(MultipartFile file, Integer userId) {
        UUID imageUuid = UUID.randomUUID();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String uploadedFilePath = String.format("%s\\uploads\\%s.%s", System.getProperty("user.dir"), imageUuid, extension);

        try {
            file.transferTo(new File(uploadedFilePath));
        } catch (IOException ex) {
            throw new FileSaveException("Could not save uploaded image!");
        }

        String response = callPythonScript(uploadedFilePath);
        if (response == null) {
            throw new FileProcessException("Uploaded image could not be processed!");
        }

        ReceiptScannedDto scanned = JsonUtil.jsonStringToReceiptEntity(response);
        if (scanned == null) {
            throw new FileProcessException("Uploaded image could not be processed!");
        }

        return processScanResult(scanned);
    }

    private void saveReceiptItems(ReceiptModel extracted, ReceiptEntity entity) {
        for (int i = 0; i < extracted.getItemNames().size(); i++) {
            ReceiptItemEntity itemEntity = ReceiptItemEntity.builder()
                    .receipt(entity)
                    .itemName(extracted.getItemNames().get(i))
                    .itemPrice(extracted.getItemPrices().get(i))
                    .build();

            receiptItemRepository.save(itemEntity);
        }
    }

    private ReceiptModel processScanResult(ReceiptScannedDto scanned) {
        ReceiptModel extracted = ReceiptMapper.scannedDtoToModel(scanned);

        String itemString = scanned.getItems();
        String[] itemStringSplitArr = itemString.split(";");

        List<String> itemNames = new ArrayList<>();
        List<Double> itemPrices = new ArrayList<>();

        for (String item : itemStringSplitArr) {
            String[] splitItem = item.split(":");
            itemNames.add(splitItem[0]);
            itemPrices.add(Double.parseDouble(splitItem[1]));
        }

        extracted.setItemNames(itemNames);
        extracted.setItemPrices(itemPrices);

        return extracted;
    }

    private String callPythonScript(String filePath) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", String.format("%s\\scripts\\main.py", System.getProperty("user.dir")), "java", filePath);

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String lines;
            String responseLines = "";
            while ((lines = reader.readLine()) != null) {
                System.out.println("Lines: " + lines);
                responseLines += lines + "\n";
            }

            boolean hasErrors = false;
            while ((lines = errorReader.readLine()) != null)  {
                System.out.println("Error Lines: " + lines);
                hasErrors = true;
            }

            if (hasErrors) {
                return null;
            }

            return responseLines;

        } catch (IOException e) {
            throw new ScriptException("Error running Python script: " + e);
        }
    }

    public ReceiptEntity getOne() {
        return receiptRepository.getOne();
    }
}
