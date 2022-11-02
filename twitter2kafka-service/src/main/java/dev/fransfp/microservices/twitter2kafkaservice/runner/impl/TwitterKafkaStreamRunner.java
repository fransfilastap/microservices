package dev.fransfp.microservices.twitter2kafkaservice.runner.impl;


import dev.fransfp.microservices.appconfig.Twitter2KafkaServiceConfig;
import dev.fransfp.microservices.twitter2kafkaservice.listener.TwitterKafkaStatusListener;
import dev.fransfp.microservices.twitter2kafkaservice.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Component
@ConditionalOnExpression("not ${twitter-to-kafka-service.enable-mock-tweets} && not ${twitter-to-kafka-service.enable-v2-tweets}")
public class TwitterKafkaStreamRunner implements StreamRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);

    private final Twitter2KafkaServiceConfig twitter2KafkaServiceConfig;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private TwitterStream twitterStream;

    public TwitterKafkaStreamRunner(Twitter2KafkaServiceConfig twitter2KafkaServiceConfig, TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitter2KafkaServiceConfig = twitter2KafkaServiceConfig;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);
        String[] keywords = twitter2KafkaServiceConfig.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);

        LOGGER.info("Started filtering twitter stream for keywords {}", Arrays.toString(keywords));
    }

    @PreDestroy
    public void shutDown(){
        if(twitterStream != null){
            LOGGER.info("Closing twitter stream!");
            twitterStream.shutdown();
        }
    }
}
