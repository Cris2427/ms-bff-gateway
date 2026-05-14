package com.rednorte.bff_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class BffGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffGatewayApplication.class, args);
	}

}
