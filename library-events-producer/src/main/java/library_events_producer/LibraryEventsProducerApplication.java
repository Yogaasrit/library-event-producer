package library_events_producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "library_events_producer")
public class LibraryEventsProducerApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibraryEventsProducerApplication.class, args);
	}
}
