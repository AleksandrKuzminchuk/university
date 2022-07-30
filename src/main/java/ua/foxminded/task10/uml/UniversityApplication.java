package ua.foxminded.task10.uml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UniversityApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityApplication.class, args);
		System.out.println("Get here-> http://localhost:8080/swagger-ui.html");
	}
}
