package com.personalfinancemanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.personalfinancemanager.domain.dto.ReceiptScannedModel;

public class JsonUtil {

    private static ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    public static ReceiptScannedModel jsonStringToReceiptEntity(String jsonString) {
        try {
            //System.out.println(objectMapper.readValue("{\"filePath\": \"bon/IMG_0010.jpg-out.txt\", \"items\": \"MAZARE BONDUSO0G:32.67; TEDT NECTAR 0.2L:1.79;R.HUMMUS ROSIT U200G-:7.39;FOLIE ALUMINIU 10M:5.99;FINO HARTIE 16BUC .:9.89;POMELO f *:9.84;PASTARNAC ,:2.72;K-BIO USTUROI 100G:4.99;CASTRAVETE FABIO BUC:3.98;VOR .LAP.3-541L:5.49;KLC MOZZ RASA200:8.99;\", \"calculatedTotal\": 93.74, \"detectedTotal\": 88.84, \"retailer\": \"KAUFLAND\", \"receiptDate\": null}", ReceiptScannedModel.class));
            //System.out.println(objectMapper.readValue("{\"filePath\": \"bon/IMG_0015.jpg-out.txt\", \"items\": \"STRUGURI. ROZ:8.16;MARLBORO RED KS:47.0;\", \"calculatedTotal\": 55.16, \"detectedTotal\": 55.26, \"retailer\": \"PROFI\", \"receiptDate\": \"2022-12-02T10:53:24\"}\n", ReceiptScannedModel.class));

            return objectMapper.readValue(jsonString, ReceiptScannedModel.class);
        } catch (JsonProcessingException e) {
            System.out.println("Scanned JSON parse exception: " + e);
            return null;
        }
    }
}
