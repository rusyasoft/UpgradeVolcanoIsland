package io.github.rusyasoft.upgrade.volcano.service;

import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;

public interface IslandBookingService {
    boolean tryToReserve(ReservationEntity reservationEntity);
    boolean cancelReservation(ReservationEntity reservationEntity);
}
