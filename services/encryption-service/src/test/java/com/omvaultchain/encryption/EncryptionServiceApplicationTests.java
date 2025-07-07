package com.omvaultchain.encryption;

import com.omvaultchain.service.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.Arrays;

@SpringBootTest
class EncryptionServiceApplicationTests {

	private AESService aesService;
	private AsymmetricEncryptionService asymmetricEncryptionService;
	private FileHashService fileHashService;
	private CryptoOrchestrator orchestrator;
	private KeyEnvelopeBuilder keyEnvelopeBuilder;

	@BeforeEach
	public void setUp() {
		aesService = new AESService();
		asymmetricEncryptionService = new AsymmetricEncryptionService();
		fileHashService = new FileHashService();
		keyEnvelopeBuilder = new KeyEnvelopeBuilder(asymmetricEncryptionService);

		orchestrator = new CryptoOrchestrator();
		orchestrator.AES_Service = aesService;
		orchestrator.AE_Service = asymmetricEncryptionService;
		orchestrator.FileHash_Service = fileHashService;
		orchestrator.IV_Generator = new IVGenerator();
		orchestrator.KeyEnvelope_Service = keyEnvelopeBuilder;
	}

	@Test
	public void testAESServiceEncryptDecrypt() throws Exception {
		String text = "Test AES encryption";
		byte[] data = text.getBytes(StandardCharsets.UTF_8);

		SecretKey key = aesService.generateKey();
		byte[] iv = IVGenerator.generateIV();

		byte[] encrypted = aesService.encrypt(data, key, iv);
		byte[] decrypted = aesService.decrypt(encrypted, key, iv);

		assertNotNull(encrypted);
		assertFalse(Arrays.equals(data, encrypted));
		assertArrayEquals(data, decrypted);
	}

	@Test
	public void testAsymmetricEncryptionWrapUnwrap() throws Exception {
		SecretKey aesKey = aesService.generateKey();

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keyPair = keyGen.generateKeyPair();

		byte[] wrappedKey = asymmetricEncryptionService.wrapAESKey(aesKey, keyPair.getPublic());
		byte[] unwrappedKey = asymmetricEncryptionService.unwrapAESKey(wrappedKey, keyPair.getPrivate());

		assertArrayEquals(aesKey.getEncoded(), unwrappedKey);
	}

	@Test
	public void testFileHashService() throws Exception {
		byte[] data = "Hash this content".getBytes(StandardCharsets.UTF_8);
		String hash = fileHashService.computeSHA256(data);

		assertNotNull(hash);
		assertEquals(64, hash.length()); // SHA-256 = 64 hex chars
	}

	@Test
	public void testCryptoOrchestratorWorkflow() throws Exception {
		byte[] fileData = "Secret File Content".getBytes(StandardCharsets.UTF_8);

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keyPair = keyGen.generateKeyPair();

		Map<String, PublicKey> publicKeys = new HashMap<>();
		publicKeys.put("user1", keyPair.getPublic());

		var response = orchestrator.encryptFile(fileData, publicKeys);

		assertNotNull(response);
		assertEquals(64, response.getHashedData().length());
		assertNotNull(response.getEncryptedData());
		assertNotNull(response.getIV());
		assertNotNull(response.getEncryptedKeys());
		assertTrue(response.getEncryptedKeys().containsKey("user1"));
	}

}
