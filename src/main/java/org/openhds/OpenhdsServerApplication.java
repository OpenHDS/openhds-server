package org.openhds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class OpenhdsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenhdsServerApplication.class, args);
	}

}
