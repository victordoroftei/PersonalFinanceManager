package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.dto.ReceiptModel;
import com.personalfinancemanager.domain.dto.ReceiptScannedModel;
import com.personalfinancemanager.domain.entity.ReceiptEntity;
import com.personalfinancemanager.domain.entity.ReceiptItemEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.repository.ReceiptItemRepository;
import com.personalfinancemanager.repository.ReceiptRepository;
import com.personalfinancemanager.exception.FileProcessException;
import com.personalfinancemanager.exception.FileSaveException;
import com.personalfinancemanager.exception.ScriptException;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.util.JsonUtil;
import com.personalfinancemanager.util.mapper.ReceiptMapper;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptItemRepository receiptItemRepository;

    private final UserRepository userRepository;

    public List<ReceiptModel> getReceiptsForMonthAndYear(Integer year, Integer month, Integer userId) {
        List<ReceiptEntity> entities = receiptRepository.findAllByUserId(userId);
        List<ReceiptEntity> filteredEntities = entities.stream()
                .filter(x -> x.getReceiptDate().getYear() == year && x.getReceiptDate().getMonthValue() == month)
                .collect(Collectors.toList());

        List<ReceiptModel> models = new ArrayList<>();
        for (ReceiptEntity entity : filteredEntities) {
            models.add(getFullReceiptModelForReceiptId(entity.getId()));
        }

        return models;
    }

    public Set<Integer> getPossibleReceiptYears(Integer userId) {
        List<ReceiptEntity> entities = receiptRepository.findAllByUserId(userId);
        Set<Integer> possibleYears = new TreeSet<>();
        entities.forEach(x -> {
            possibleYears.add(x.getReceiptDate().getYear());
        });

        return possibleYears;
    }

    private ReceiptModel getFullReceiptModelForReceiptId(Integer receiptId) {
        Optional<ReceiptEntity> receiptEntityOptional = receiptRepository.findById(receiptId);
        if (receiptEntityOptional.isPresent()) {
            ReceiptEntity receiptEntity = receiptEntityOptional.get();
            ReceiptModel model = ReceiptMapper.entityToModel(receiptEntity);

            List<ReceiptItemEntity> items = receiptItemRepository.findAllByReceiptId(receiptId);
            List<Double> itemPrices = new ArrayList<>();
            List<String> itemNames = new ArrayList<>();
            for (ReceiptItemEntity item : items) {
                itemPrices.add(item.getItemPrice());
                itemNames.add(item.getItemName());
            }

            model.setItemNames(itemNames);
            model.setItemPrices(itemPrices);

            return model;
        }

        return null;
    }

    public void addReceipt(ReceiptModel model, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            ReceiptEntity receiptEntity = ReceiptMapper.modelToEntity(model);

            receiptEntity.setUser(userOptional.get());
            receiptEntity.setInsertedDate(LocalDateTime.now());
            receiptEntity.setImagePath(getImagePathFromFullPath(receiptEntity.getImagePath()));

            receiptRepository.save(receiptEntity);
            saveReceiptItems(model, receiptEntity);
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

        ReceiptScannedModel scanned = JsonUtil.jsonStringToReceiptEntity(response);
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

    private ReceiptModel processScanResult(ReceiptScannedModel scanned) {
        ReceiptModel extracted = ReceiptMapper.scannedModelToModel(scanned);

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

    private String getImagePathFromFullPath(String imagePath) {
        String[] splitArr = imagePath.split("\\\\");
        return splitArr[splitArr.length - 1];
    }
}
