package com.personalfinancemanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.personalfinancemanager.domain.model.ReceiptScannedModel;

public class JsonUtil {

    private static ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    public static ReceiptScannedModel jsonStringToReceiptEntity(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, ReceiptScannedModel.class);
        } catch (JsonProcessingException e) {
            System.out.println("Scanned JSON parse exception: " + e);
            return null;
        }
    }
}
