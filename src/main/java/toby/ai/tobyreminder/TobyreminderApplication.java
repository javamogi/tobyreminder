package toby.ai.tobyreminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TobyreminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TobyreminderApplication.class, args);
	}

}
