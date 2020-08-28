package com.demo;

import java.security.Provider;
import java.security.Security;

public class EnvironmentConfigurer {


    public static void configure() {

        try {
            System.out.println("Inside configure block!!!!!!!!!!!!!!!");
            loadProviderAt("org.bouncycastle.jce.provider.BouncyCastleProvider", 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadProviderAt(String providerClassName, int position) throws Exception {
        System.out.println("Loading provider class..........");
        final Class providerClass = Class.forName(providerClassName);
        System.out.println("Create provider instance..........");
        final Provider provider = (Provider) providerClass.getConstructor().newInstance();
        System.out.println("Insert provider instance..........");
        final int finalPosition = Security.insertProviderAt(provider, position);
    }
}
