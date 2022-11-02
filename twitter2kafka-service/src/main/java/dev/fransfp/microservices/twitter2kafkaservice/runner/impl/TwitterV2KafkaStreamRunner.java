package dev.fransfp.microservices.twitter2kafkaservice.runner.impl;

import dev.fransfp.microservices.appconfig.Twitter2KafkaServiceConfig;
import dev.fransfp.microservices.twitter2kafkaservice.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnExpression("not ${twitter-to-kafka-service.enable-mock-tweets} && ${twitter-to-kafka-service.enable-v2-tweets}")
public class TwitterV2KafkaStreamRunner implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterV2KafkaStreamRunner.class);
    private final Twitter2KafkaServiceConfig twitter2KafkaServiceConfig ;
    private final TwitterV2StreamHelper twitterV2StreamHelper;

    public TwitterV2KafkaStreamRunner(Twitter2KafkaServiceConfig twitter2KafkaServiceConfig, TwitterV2StreamHelper twitterV2StreamHelper) {
        this.twitter2KafkaServiceConfig = twitter2KafkaServiceConfig;
        this.twitterV2StreamHelper = twitterV2StreamHelper;
    }

    @Override
    public void start() throws TwitterException {

        String bearerToken = twitter2KafkaServiceConfig.getTwitterV2BearerToken();
        if(null!=bearerToken){
            try{
                twitterV2StreamHelper.setupRules(bearerToken,getRules());
                twitterV2StreamHelper.connectStream(bearerToken);

            }catch (IOException | URISyntaxException e){
                LOG.error("Error streaming tweets!",e);
                throw new RuntimeException("Error streaming tweets!",e);
            }

        }else{
            LOG.error("There was a problem with your bearer token."+
                    "Please make sure you set the TWITTER_BEARER_TOKEN environment variable"
            );

            throw new RuntimeException("Ther was a problem getting your bearer token."+
                    "Please make sure you set the TWITTER_BEARER_TOKEN environment variable"
                    );
        }

    }

    private Map<String, String> getRules(){
        List<String> keywords = twitter2KafkaServiceConfig.getTwitterKeywords();
        Map<String, String> rules = new HashMap<>();
        for(String keyword: keywords){
            rules.put(keyword, "Keyword: "+keyword);
        }

        LOG.info("Created filter for twitter stream for keywords: {}",keywords);

        return rules;
    }
}
