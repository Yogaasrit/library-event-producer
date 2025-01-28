package library_events_producer.controller;

import library_events_producer.domain.LibraryEvent;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class LibraryEventController {
    private static final Logger logger = Logger.getLogger(LibraryEventController.class.getName());

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(
            @RequestBody LibraryEvent libraryEvent
    ){
        logger.info("Library Event: " + libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
