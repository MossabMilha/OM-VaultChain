package com.omvaultchain.encryption;

import com.omvaultchain.service.AESService;
import com.omvaultchain.service.AsymmetricEncryptionService;
import com.omvaultchain.service.IVGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


@SpringBootApplication
public class EncryptionServiceApplication {
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public void RSA() throws Exception{
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair pair = generator.generateKeyPair();
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EncryptionServiceApplication.class, args);

		// Initialize app and generate RSA key pair
		EncryptionServiceApplication app = new EncryptionServiceApplication();
		app.RSA();

		System.out.println("The Public Key : " + Base64.getEncoder().encodeToString(app.privateKey.getEncoded()));
		System.out.println("The Private Key : " + Base64.getEncoder().encodeToString(app.publicKey.getEncoded()));

		// Create services
		AESService AES_Service = new AESService();
		AsymmetricEncryptionService AE_Service = new AsymmetricEncryptionService();

		// Generate AES key and IV
		SecretKey key = AES_Service.generateKey();
		byte[] iv = IVGenerator.generateIV();

		// Encrypt and decrypt sample data
		byte[] plaintext = "Hello World".getBytes();
		byte[] encryptedData = AES_Service.encrypt(plaintext, key, iv);
		byte[] decryptedData = AES_Service.decrypt(encryptedData, key, iv);

		System.out.println("The Key : " + Base64.getEncoder().encodeToString(key.getEncoded()));
		System.out.println("Encrypted Data (Base64): " + Base64.getEncoder().encodeToString(encryptedData));
		System.out.println("Decrypted Data (String) : " + new String(decryptedData));

		// Wrap and unwrap AES key using RSA
		byte[] wrappedKey = AE_Service.wrapAESKey(key.getEncoded(), app.publicKey);
		byte[] unwrappedKeyBytes = AE_Service.unwrapAESKey(wrappedKey, app.privateKey);
		SecretKey keyDecrypted = new SecretKeySpec(unwrappedKeyBytes, "AES");

		// Final decryption with unwrapped key
		byte[] decryptedData_1 = AES_Service.decrypt(encryptedData, keyDecrypted, iv);

		System.out.println("The Key Encrypted : " + Base64.getEncoder().encodeToString(wrappedKey));
		System.out.println("The Key : " + Base64.getEncoder().encodeToString(keyDecrypted.getEncoded()));
		System.out.println("Decrypted Data (String) : " + new String(decryptedData_1));


    }





}
