package com.personalfinancemanager;

import com.personalfinancemanager.util.JsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersonalFinanceManagerApplication {

    public static void main(String[] args) {
        JsonUtil.jsonStringToReceiptEntity("a");
        SpringApplication.run(PersonalFinanceManagerApplication.class, args);
    }
}
