package de.nodeline.box;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import de.nodeline.box.application.primaryadapter.api.PrimaryApiExceptionHandler;

@SpringBootApplication
@Import(PrimaryApiExceptionHandler.class)
public class BoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoxApplication.class, args);
	}

}
