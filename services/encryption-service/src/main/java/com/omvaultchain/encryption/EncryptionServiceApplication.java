package com.omvaultchain.encryption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class EncryptionServiceApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EncryptionServiceApplication.class, args);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        System.out.println("PUBLIC KEY (Base64):");
        System.out.println(publicKeyBase64);
        System.out.println("\nPRIVATE KEY (Base64):");
        System.out.println(privateKeyBase64);


    }





}
