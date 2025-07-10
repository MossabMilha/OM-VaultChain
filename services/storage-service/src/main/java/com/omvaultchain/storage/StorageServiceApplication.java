package com.omvaultchain.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.omvaultchain.storage")
public class StorageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StorageServiceApplication.class, args);
	}
}
