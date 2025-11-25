package com.fierceadventurer.keycloakeventlistener.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;



public class KafkaEventListenerProvider implements EventListenerProvider {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListenerProvider.class);
    private final KafkaProducer<String , String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KeycloakSession session;

    public KafkaEventListenerProvider(KafkaProducer<String, String> producer, KeycloakSession session) {
        this.producer = producer;
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        try {
            if(EventType.REGISTER.equals(event.getType())){
                log.info("Caught REGISTER event for User ID: {}", event.getUserId());
                sendUserToKafka(event.getUserId(), event.getRealmId());
            }
        } catch (Exception e) {
            log.info("CRITICAL: Failed to process Kafka event, but allowing user creation to proceed.");
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        try {
            if (ResourceType.USER.equals(adminEvent.getResourceType())
                    && OperationType.CREATE.equals(adminEvent.getOperationType())) {

                String path = adminEvent.getResourcePath();
                String userId = path.startsWith("users/") ? path.substring(6) : path;

                log.info("Caught ADMIN USER CREATE for User ID: {}", userId);
                sendUserToKafka(userId, adminEvent.getRealmId());
            }
        } catch (Exception e) {
            log.error("CRITICAL: Kafka Plugin failed (Admin), but allowing user creation.", e);

        }
    }

    private void sendUserToKafka(String userId, String realmId) {

        if (this.producer == null) {
            log.error("Kafka Producer is NULL. Check Factory logs.");
            return;
        }
        try{
            RealmProvider realmProvider = session.realms();
            UserModel user = session.users().getUserById(realmProvider.getRealm(realmId),userId);

            if(user == null){
                log.error("user not found in session: {}" , userId);
                return;
            }

            Map<String , Object> userMap = new HashMap<>();
            userMap.put("userId" , user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("username" , user.getUsername());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());

            String jsonPayload = objectMapper.writeValueAsString(userMap);

            ProducerRecord<String , String> record = new ProducerRecord<>("keycloak.user.created" , user.getId() , jsonPayload);
            producer.send(record , (metadata , exception) -> {
                if (exception != null){
                    log.error("Failed to send user to Kafka", exception);
                }
                else {
                    log.info("SYNC SUCCESS: sent user {} to topic {}" , userId , metadata.topic());
                }
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to sync user to Kafka", e);
            e.printStackTrace();

        }
    }

    @Override
    public void close() {

    }
}
