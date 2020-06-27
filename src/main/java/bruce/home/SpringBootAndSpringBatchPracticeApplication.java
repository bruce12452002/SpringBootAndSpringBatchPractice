package bruce.home;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBootAndSpringBatchPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAndSpringBatchPracticeApplication.class, args);
	}

}
