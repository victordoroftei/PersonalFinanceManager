package com.personalfinancemanager.service;

import com.personalfinancemanager.exception.ScriptException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class TestService {

    public void callPythonScript(String filePath) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", String.format("%s\\scripts\\main.py", System.getProperty("user.dir")), "java", filePath);

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String lines;
            while ((lines = reader.readLine()) != null) {
                System.out.println("Lines: " + lines);
            }

            while ((lines = errorReader.readLine()) != null)  {
                System.out.println("Error Lines: " + lines);
            }

        } catch (IOException e) {
            throw new ScriptException("Error running Python script: " + e);
        }
    }
}
