package com.omvaultchain.encryption;

import com.omvaultchain.model.KeyEnvelope;
import com.omvaultchain.service.AESService;
import com.omvaultchain.service.AsymmetricEncryptionService;
import com.omvaultchain.service.IVGenerator;
import com.omvaultchain.service.KeyEnvelopeBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


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

		// ----------------- Add KeyEnvelopeBuilder test ------------------

		KeyEnvelopeBuilder keyEnvelopeBuilder = new KeyEnvelopeBuilder(AE_Service);

		// Let's assume 2 recipients: "user1" and "user2"
		KeyPairGenerator generator2 = KeyPairGenerator.getInstance("RSA");
		generator2.initialize(2048);

		KeyPair pair1 = generator2.generateKeyPair();
		KeyPair pair2 = generator2.generateKeyPair();

		PublicKey pub1 = pair1.getPublic();
		PublicKey pub2 = pair2.getPublic();

		Map<String, PublicKey> recipients = new HashMap<>();
		recipients.put("user1_wallet_address", pub1);
		recipients.put("user2_wallet_address", pub2);

		// AES key to share
		byte[] aesKeyBytes = key.getEncoded(); // From earlier AES generation
		KeyEnvelope envelope = keyEnvelopeBuilder.buildEnvelope(aesKeyBytes, recipients);

		// Show the encrypted keys per user
		System.out.println("\n===== Key Envelope Results =====");
		Map<String, PrivateKey> privateKeys = new HashMap<>();
		privateKeys.put("user1_wallet_address", pair1.getPrivate());
		privateKeys.put("user2_wallet_address", pair2.getPrivate());
		for (Map.Entry<String, byte[]> entry : envelope.getEncryptedKeys().entrySet()) {
			String recipient = entry.getKey();
			byte[] encryptedAESKey = entry.getValue();

			System.out.println("Recipient: " + recipient);
			System.out.println("Encrypted AES Key (Base64): " + Base64.getEncoder().encodeToString(encryptedAESKey));

			// Decrypt AES key with recipient's private key
			PrivateKey recipientPrivateKey = privateKeys.get(recipient);
			byte[] decryptedAESKeyBytes = AE_Service.unwrapAESKey(encryptedAESKey, recipientPrivateKey);
			SecretKey decryptedAESKey = new SecretKeySpec(decryptedAESKeyBytes, "AES");

			// Use decrypted AES key to decrypt the original encrypted data
			byte[] decryptedDataForRecipient = AES_Service.decrypt(encryptedData, decryptedAESKey, iv);

			System.out.println("Decrypted AES Key (Base64): " + Base64.getEncoder().encodeToString(decryptedAESKeyBytes));
			System.out.println("Decrypted Data: " + new String(decryptedDataForRecipient));
			System.out.println("--------------------------------------------------");
		}


    }





}
