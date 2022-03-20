package io.github.rusyasoft.upgrade.volcano.queue.producer;

import io.github.rusyasoft.upgrade.volcano.queue.ReservationProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaReservationProducer extends ReservationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReservationProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${island.kafka.reserve-topic}")
    private String topic;

    @Autowired
    public KafkaReservationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(final String resId) {
        LOGGER.info("sending resId='{}' to topic='{}'", resId, topic);
        kafkaTemplate.send(topic, resId);
    }
}