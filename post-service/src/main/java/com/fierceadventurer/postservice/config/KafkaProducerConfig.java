package com.fierceadventurer.postservice.config;

import com.fierceadventurer.postservice.events.VariantReadyForSchedulingEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, VariantReadyForSchedulingEvent> postFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(null));
    }

    @Bean
    public KafkaTemplate<String , VariantReadyForSchedulingEvent> kafkaTemplate(
            ProducerFactory<String, VariantReadyForSchedulingEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
