package library_events_producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import library_events_producer.domain.LibraryEvent;

import library_events_producer.producer.LibraryEventsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
public class LibraryEventController {
    private static final Logger logger = Logger.getLogger(LibraryEventController.class.getName());
    public final LibraryEventsProducer libraryEventProducer;
    public LibraryEventController(LibraryEventsProducer libraryEventProducer) {
        this.libraryEventProducer = libraryEventProducer;
    }

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        logger.info("Library Event: " + libraryEvent);

//        libraryEventProducer.sendLibraryEventApproach1(libraryEvent);
//        libraryEventProducer.sendLibraryEventApproach2(libraryEvent);
//        libraryEventProducer.sendLibraryEventApproach3(libraryEvent);

        logger.info("After sending library event");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
