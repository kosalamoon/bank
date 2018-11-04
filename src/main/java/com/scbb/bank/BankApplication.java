package com.scbb.bank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackageClasses = {BankApplication.class/*, Jsr310JpaConverters.class*/})
@EnableScheduling
public class BankApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+5:30"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@Transactional
	public CommandLineRunner run(RestTemplate restTemplate) {
		return args -> {
			String s = LocalDate.now().toString() + " at " + String.valueOf(LocalTime.now().getHour()) + ":" + String.valueOf(LocalTime.now().getHour());
			System.out.println(s);
			Date date = new Date();
			System.out.println(date);
		};
	}

}
