package io.github.rusyasoft.upgrade.volcano.queue;

public abstract class ReservationConsumer {
    public abstract void consume(Integer resId);
}
