package br.com.raroacademy.projetofinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "br.com.raroacademy.projetofinal.client")
@SpringBootApplication
public class ProjetofinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetofinalApplication.class, args);
	}

}
