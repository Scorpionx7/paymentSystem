package com.esther.payment_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PaymentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentSystemApplication.class, args);
	}

}
