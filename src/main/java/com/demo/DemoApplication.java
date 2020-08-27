package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods=false)
public class DemoApplication {
	private final static byte[] IV = { 17, 15, 9, 5, 13, 3, 18, 12, 18, 2, 19, 20, 6, 18, 1, 5 };
	private final static String SECRET_KEY = "PBKDF2WithHmSHA1";


	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);
        System.out.println("Boot 2.3.1.RELEASE with Tomcat!!!!!!!!!!!!!!!");
		String data = "My data";
        String encryptedString = new CryptoService(IV,SECRET_KEY.getBytes()).encrypt(data);
		System.out.println("Encrypted String: "+ encryptedString);
		String decryptedString = new CryptoService(IV,SECRET_KEY.getBytes()).decrypt(encryptedString);
		System.out.println("Decrypted String: "+ decryptedString);
	}



}
