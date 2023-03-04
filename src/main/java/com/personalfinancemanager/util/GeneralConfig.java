package com.personalfinancemanager.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableConfigurationProperties
@Getter
@Setter
public class GeneralConfig {

    @Value("${spring.jwt.public.key}")
    private RSAPublicKey key;

    @Value("${spring.jwt.private.key}")
    private RSAPrivateKey priv;
}
