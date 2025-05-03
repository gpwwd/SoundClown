package com.soundclown.application;

import com.soundclown.application.infrastructure.environment.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.soundclown")
public class Application {

	public static void main(String[] args) {
		EnvLoader.load();
		SpringApplication.run(Application.class, args);
	}

}
