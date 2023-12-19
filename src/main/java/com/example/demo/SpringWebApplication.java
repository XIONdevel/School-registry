package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication(scanBasePackages = "com.example.demo")
public class SpringWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebApplication.class, args);
	}

//public static void main(String[] args) {
//	int keyLength = 256; // Adjust the length based on your requirements
//	byte[] jwtSecretBytes = generateRandomBytes(keyLength);
//	String jwtSecretHex = bytesToHex(jwtSecretBytes);
//	System.out.println("Generated JWT Secret in HEX: " + jwtSecretHex);
//}
//
//	private static byte[] generateRandomBytes(int length) {
//		SecureRandom random = new SecureRandom();
//		byte[] bytes = new byte[length];
//		random.nextBytes(bytes);
//		return bytes;
//	}
//
//	private static String bytesToHex(byte[] bytes) {
//		StringBuilder hexString = new StringBuilder(2 * bytes.length);
//		for (byte b : bytes) {
//			hexString.append(String.format("%02x", b & 0xff));
//		}
//		return hexString.toString().toUpperCase();
//	}
}
