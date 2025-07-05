package com.omvaultchain.encryption;

import com.omvaultchain.service.AESService;
import com.omvaultchain.service.IVGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Base64;


@SpringBootApplication
public class EncryptionServiceApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EncryptionServiceApplication.class, args);
		AESService AES_Service = new AESService();
		SecretKey key = AES_Service.generateKey();

		byte[] iv = IVGenerator.generateIV();// Generate The IV KEy
		byte[] plaintext = "Hello World".getBytes();// The Data To Crypt
		byte[] encryptedData = AES_Service.encrypt(plaintext, key, iv);// Encrypted Data
		byte[] decryptedData = AES_Service.decrypt(encryptedData, key, iv);// Decrypted Data

		System.out.println("The Key : "+ Base64.getEncoder().encodeToString(key.getEncoded()));
		System.out.println("Encrypted Data (Base64): " + Base64.getEncoder().encodeToString(encryptedData));
		System.out.println("Decrypted Data (String) : "+new String(decryptedData));


    }





}
