package io.github.rusyasoft.upgrade.volcano.tools;

import io.github.rusyasoft.upgrade.volcano.model.BookingState;
import io.github.rusyasoft.upgrade.volcano.model.Contact;
import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;

import java.util.Date;

public class ReservationBuilder {
    public static ReservationEntity newReservationEntity(Contact contact, Date from, Date to) {
        ReservationEntity newReservation = ReservationEntity.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .bookingState(BookingState.BOOKING)
                .fromDate(from)
                .toDate(to)
                .build();

        return newReservation;
    }

    public static ReservationEntity updatedReservationEntity(int resId, Contact contact, Date from, Date to) {
        ReservationEntity updateReservation = newReservationEntity(contact, from, to);
        updateReservation.setResId(resId);

        return updateReservation;
    }

    public static ReservationEntity updatedReservationEntity(int resId, Contact contact, Date from, Date to,
                                                             BookingState state) {
        ReservationEntity updateReservation = updatedReservationEntity(resId, contact, from, to);
        updateReservation.setBookingState(state);

        return updateReservation;
    }

    public static ReservationEntity updatedReservationEntity(ReservationEntity reservationEntity,
                                                             BookingState state) {
        reservationEntity.setBookingState(state);
        return reservationEntity;
    }

}
