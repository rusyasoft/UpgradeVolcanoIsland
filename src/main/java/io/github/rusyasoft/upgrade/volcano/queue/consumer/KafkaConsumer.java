package io.github.rusyasoft.upgrade.volcano.queue.consumer;

import io.github.rusyasoft.upgrade.volcano.queue.ReservationConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private ReservationConsumer reservationConsumer;

    @Autowired
    public KafkaConsumer(ReservationConsumer reservationConsumer) {
        this.reservationConsumer = reservationConsumer;
    }

    @KafkaListener(topics = "${island.kafka.reserve-topic}")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        LOGGER.info("received payload='{}'", consumerRecord.value().toString());
        int resId = Integer.valueOf(consumerRecord.value().toString());
        reservationConsumer.consume(resId);
    }
}