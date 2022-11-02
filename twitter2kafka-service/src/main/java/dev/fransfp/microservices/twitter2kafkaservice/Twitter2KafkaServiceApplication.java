package dev.fransfp.microservices.twitter2kafkaservice;

import dev.fransfp.microservices.appconfig.Twitter2KafkaServiceConfig;
import dev.fransfp.microservices.twitter2kafkaservice.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "dev.fransfp.microservices")
public class Twitter2KafkaServiceApplication implements CommandLineRunner {

    private final Logger LOG = LoggerFactory.getLogger(Twitter2KafkaServiceApplication.class);
    private final Twitter2KafkaServiceConfig twitter2KafkaServiceConfig;
    private final StreamRunner streamRunner;

    public Twitter2KafkaServiceApplication(Twitter2KafkaServiceConfig twitter2KafkaServiceConfig,StreamRunner streamRunner) {
        this.twitter2KafkaServiceConfig = twitter2KafkaServiceConfig;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args){
        SpringApplication.run(Twitter2KafkaServiceApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Application Starts...");
        LOG.info(Arrays.toString(twitter2KafkaServiceConfig.getTwitterKeywords().toArray(new String[]{})));
        streamRunner.start();
    }
}
