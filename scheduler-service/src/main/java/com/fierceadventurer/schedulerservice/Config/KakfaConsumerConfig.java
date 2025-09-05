package com.fierceadventurer.schedulerservice.Config;

import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KakfaConsumerConfig {
    @Bean
    public ConsumerFactory<String , VariantReadyForSchedulingEvent> consumerFactory(
            KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(null));

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory
            <String , VariantReadyForSchedulingEvent> kafkaListenerContainerFactory(
                    ConsumerFactory<String, VariantReadyForSchedulingEvent> consumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<String , VariantReadyForSchedulingEvent>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;

    }
}
