package library_events_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import library_events_producer.domain.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Component
public class LibraryEventsProducer {
    @Value("${kafka.topic}")
    public String topic;

    private final KafkaTemplate<Integer,String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = Logger.getLogger(LibraryEventsProducer.class.getName());

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendLibraryEventApproach1(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.libraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        /* When this call is made, two things happen begin the scenes
            1. Blocking call - get the metadata about the kafka cluster
            2. Send message happens - which is a completable future
            Async behavior - Preferable approach
            O/P : Library Event: LibraryEvent[libraryEventId=null, libraryEventType=NEW, book=Book[bookId=456, bookName=Kafka Using Spring Boot, bookAuthor=Dilip]]
                  After sending library event
                  Message Sent SuccessFully for the key : null
        */
        var completableFuture =  kafkaTemplate.send(topic, key, value);
        completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if (throwable != null) {
                        handleFailure(key, value, throwable);
                    } else {
                        handleSuccess(key, value, sendResult);
                    }
                });
    }

    public SendResult<Integer,String> sendLibraryEventApproach2(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        Integer key = libraryEvent.libraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        /* When this call is made, two things happen begin the scenes
            1. Blocking call - get the metadata about the kafka cluster
            2. Block and wait until the message is sent to the kafka cluster
            Sync behavior
            O/P : Library Event: LibraryEvent[libraryEventId=null, libraryEventType=NEW, book=Book[bookId=456, bookName=Kafka Using Spring Boot, bookAuthor=Dilip]]
                  Message Sent SuccessFully for the key : null
                  After sending library event
        */
        var sendResult =  kafkaTemplate.send(topic, key, value).get();
        handleSuccess(key,value,sendResult);
        return sendResult;
    }

    public void sendLibraryEventApproach3(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.libraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        var producerRecord = buildProducerRecord(key,value);
        /* Async approach
            using producerRecord
            O/P: Library Event: LibraryEvent[libraryEventId=null, libraryEventType=NEW, book=Book[bookId=456, bookName=Kafka Using Spring Boot, bookAuthor=Dilip]]
                 After sending library event
                 Message Sent SuccessFully for the key : null
        */
        var completableFuture = kafkaTemplate.send(producerRecord);
        completableFuture.whenComplete((sendResult,throwable)->{
            if(throwable!=null){
                handleFailure(key, value, throwable);
            }else{
                handleSuccess(key, value, sendResult);
            }
        });
    }

    private ProducerRecord<Integer,String> buildProducerRecord(Integer key, String value) {
        return new ProducerRecord<>(topic,key,value);
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        logger.info("Message Sent SuccessFully for the key "+ key);
    }

    private void handleFailure(Integer key, String value, Throwable throwable) {
        logger.info("Error sending the message and the exception " + throwable.getMessage());
    }
}
