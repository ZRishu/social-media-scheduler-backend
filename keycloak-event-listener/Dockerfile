FROM quay.io/keycloak/keycloak:latest
COPY target/keycloak-kafka-listener-1.0-SNAPSHOT.jar /opt/keycloak/providers/
RUN /opt/keycloak/bin/kc.sh build
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]