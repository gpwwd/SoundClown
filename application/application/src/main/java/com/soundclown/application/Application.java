package com.soundclown.application;

import com.soundclown.application.infrastructure.environment.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.soundclown")
@EntityScan(basePackages = {
		"com.soundclown.track"
})
@EnableJpaRepositories(basePackages = {
		"com.soundclown.track"
})
public class Application {

	public static void main(String[] args) {
		EnvLoader.load();

		SpringApplication.run(Application.class, args);
	}

}
