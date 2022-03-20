package io.github.rusyasoft.upgrade.volcano.queue.consumer;

import io.github.rusyasoft.upgrade.volcano.queue.ReservationConsumer;
import io.github.rusyasoft.upgrade.volcano.service.IslandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationConsumerImpl extends ReservationConsumer {

    private IslandService islandService;

    @Autowired
    public ReservationConsumerImpl(IslandService islandService) {
        this.islandService = islandService;
    }

    @Override
    public void consume(final Integer resId) {
        islandService.processReservation(resId);
    }
}
