package com.example.demo;

import com.example.demo.config.MongoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(DemoApplication.class);
		app.addInitializers(new MongoConfig());
		app.run(args);
	}
}