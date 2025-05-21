package com.example.SmartPot;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class SmartPotApplication {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		String firebaseConfigPath = System.getenv("FIREBASE_CONFIG_PATH");
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
				new ClassPathResource(firebaseConfigPath).getInputStream()
		);
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
		return FirebaseMessaging.getInstance(app);
	}

	public static void main(String[] args) {
		SpringApplication.run(SmartPotApplication.class, args);
	}

}
