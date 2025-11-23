package com.fierceadventurer.keycloakeventlistener.keycloak;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaEventListenerProviderFactory implements EventListenerProviderFactory {
    private static final Logger log = LoggerFactory.getLogger(KafkaEventListenerProviderFactory.class);
    private KafkaProducer<String , String> producer;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new KafkaEventListenerProvider(producer , session);
    }

    @Override
    public void init(Config.Scope scope) {
        log.info("Initializing Kafka Event Listener Factory...");

        String bootstrapServers = System.getenv("KAFKA_URL");
        String apiKey = System.getenv("KAFKA_KEY");
        String apiSecret = System.getenv("KAFKA_SECRET");

        if(bootstrapServers == null){
            log.warn("KAFKA_URL not set. Kafka Producer will NOT start");
            return;
        }

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , StringSerializer.class.getName());

        props.put("security.protocol", "SASL_SSL");
        props.put("security.mechanism" , "PLAIN");
        String jaas = String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", apiKey, apiSecret);

        props.put("sasl.jaas.config" , jaas);

        this.producer = new KafkaProducer<>(props);
        log.info("Kafka Producer initialized for Keycloak.");

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {
        if(producer != null){
            producer.close();
        }
    }

    @Override
    public String getId() {
        return "kafka-event-listener";
    }
}
