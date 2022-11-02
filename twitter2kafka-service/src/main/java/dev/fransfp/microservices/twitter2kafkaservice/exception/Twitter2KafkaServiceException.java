package dev.fransfp.microservices.twitter2kafkaservice.exception;

public class Twitter2KafkaServiceException extends RuntimeException{

    public Twitter2KafkaServiceException() {
        super();
    }

    public Twitter2KafkaServiceException(String message) {
        super(message);
    }

    public Twitter2KafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
