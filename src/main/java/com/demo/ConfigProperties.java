package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "mail")
public class ConfigProperties {
    static {
        System.out.println("ConfigProperties static block!!!!!!!!!!!!!!!");
        //EnvironmentConfigurer.configure();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        try {
            String decryptedString = new CryptoService().decrypt(secret);
            this.secret = decryptedString;
            System.out.println("************Decrypted password:************** "+this.secret);
            EnvironmentConfigurer.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String username;
    private String secret;
}
