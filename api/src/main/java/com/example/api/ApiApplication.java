package com.example.api;
import com.example.api.principal.principal;
import com.example.api.repository.iAutorRepository;
import com.example.api.repository.iLibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication implements CommandLineRunner {
	@Autowired
	private iLibroRepository libroRepository;
	@Autowired
	private iAutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		principal principal = new principal(libroRepository, autorRepository);
		principal.muestraElMenu();
	}
}
