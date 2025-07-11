package com.omvaultchain.test_service_combine;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class TestServiceCombineApplication {

	public static void main(String[] args) {
		try {
			Security.addProvider(new BouncyCastleProvider());

			// 🧠 Step 1: Provide your MetaMask private key (from export, 64 hex chars)
			String privateKeyHex = "f4ad630fd1495f48f46e27ca6814368c2d4228c938309caeb5fcec7e9cdedb3e"; // Replace with your actual private key

			// 🧠 Step 2: Convert hex key to EC PrivateKey
			byte[] privBytes = Hex.decode(privateKeyHex);
			ECNamedCurveParameterSpec ecSpec = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec("secp256k1");
			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");

			ECPrivateKeySpec priKeySpec = new ECPrivateKeySpec(new BigInteger(1, privBytes), ecSpec);
			PrivateKey privateKey = keyFactory.generatePrivate(priKeySpec);

			// 🔐 Step 3: Derive Public Key from Private Key (MetaMask doesn’t export it)
			ECPoint Q = ecSpec.getG().multiply(new BigInteger(1, privBytes));
			ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
			PublicKey publicKey = keyFactory.generatePublic(pubSpec);

			// 📝 Step 4: Create test message
			String message = "Hello World";
			byte[] messageBytes = message.getBytes();

			// 🔒 Step 5: Encrypt with public key using ECIES
			Cipher eciesEncrypt = Cipher.getInstance("ECIES", "BC");
			eciesEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encrypted = eciesEncrypt.doFinal(messageBytes);
			String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

			System.out.println("Encrypted (Base64): " + encryptedBase64);

			// 🔓 Step 6: Decrypt with private key using ECIES
			Cipher eciesDecrypt = Cipher.getInstance("ECIES", "BC");
			eciesDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decrypted = eciesDecrypt.doFinal(Base64.getDecoder().decode(encryptedBase64));
			String decryptedMessage = new String(decrypted);

			System.out.println("Decrypted: " + decryptedMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
