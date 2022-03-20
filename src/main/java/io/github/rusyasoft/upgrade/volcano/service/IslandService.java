package io.github.rusyasoft.upgrade.volcano.service;

import io.github.rusyasoft.upgrade.volcano.model.BookingState;
import io.github.rusyasoft.upgrade.volcano.model.Contact;
import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;
import io.github.rusyasoft.upgrade.volcano.model.ReservationUpdateData;
import io.github.rusyasoft.upgrade.volcano.model.ReserveDateEntity;
import io.github.rusyasoft.upgrade.volcano.queue.ReservationProducer;
import io.github.rusyasoft.upgrade.volcano.repository.ReservationRepository;
import io.github.rusyasoft.upgrade.volcano.repository.ReserveDateRepository;
import io.github.rusyasoft.upgrade.volcano.tools.ReservationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class IslandService {
    private ReserveDateRepository reserveDateRepository;
    private ReservationRepository reservationRepository;

    private IslandBookingService islandBookingService;

    @Autowired
    private ReservationProducer reservationProducer;

    @Autowired
    public IslandService(
            ReserveDateRepository reserveDateRepository,
            ReservationRepository reservationRepository,
            IslandBookingService islandBookingService
    ) {
        this.reserveDateRepository = reserveDateRepository;
        this.reservationRepository = reservationRepository;
        this.islandBookingService = islandBookingService;
    }

    public List<String> getListOfAvailableDates(Date fromDate, Date toDate) {
        List<ReserveDateEntity> allReservationDatesForPeriod
                = reserveDateRepository.findAllBySingleDateBetween(fromDate, toDate);

        List<String> notBookedDates = allReservationDatesForPeriod
                .stream()
                .filter(reserveDateEntity -> reserveDateEntity.getResId() == null)
                .map(reserveDateEntity -> reserveDateEntity.getSingleDate().toString())
                .collect(Collectors.toList());

        return notBookedDates;
    }

    public ReservationEntity getReservationStatus(Integer resId) {
        ReservationEntity reservationEntity = this.reservationRepository.findByResId(resId);

        if (reservationEntity == null) {
            throw new RuntimeException("ReservationId: " + resId + " not found");
        }
        return reservationEntity;
    }

    // it creates a new field in Reservation table with BOOKING status
    public Integer createReservation(Contact contact, Date fromDate, Date toDate) {
        ReservationEntity newReservation = ReservationBuilder.newReservationEntity(contact, fromDate, toDate);
        ReservationEntity afterSaving = this.reservationRepository.save(newReservation);

        reservationProducer.produce(afterSaving.getResId().toString());

        return afterSaving.getResId();
    }

    public void processReservation(Integer resId) {
        ReservationEntity reservationEntity = this.reservationRepository.findByResId(resId);

        if (reservationEntity == null) {
            System.out.println("ReservationId: " + resId + " doesn't contain any field");
            return;
        }

        if (reservationEntity.getBookingState() == BookingState.BOOKING) {
            if (islandBookingService.tryToReserve(reservationEntity)) {
                reservationEntity.setBookingState(BookingState.BOOKED);
            } else {
                reservationEntity.setBookingState(BookingState.FAILED);
            }
            this.reservationRepository.save(reservationEntity);
        }
    }

    public Integer updateReservation(ReservationUpdateData reservationUpdateData) {
        ReservationEntity reservationEntity = this.reservationRepository.findByResId(reservationUpdateData.getResId());

        if (reservationEntity == null) {
            throw new RuntimeException("ReservationId: " + reservationUpdateData.getResId() + " not found");
        }

        switch (reservationEntity.getBookingState()) {
            case BOOKED: islandBookingService.cancelReservation(reservationEntity);
            case FAILED: {
                ReservationEntity updateReservation = ReservationBuilder.updatedReservationEntity(
                        reservationUpdateData.getResId(),
                        reservationUpdateData.getContactAndDates().getContact(),
                        reservationUpdateData.getContactAndDates().getFromDate(),
                        reservationUpdateData.getContactAndDates().getToDate()
                );

                ReservationEntity afterSaving = this.reservationRepository.save(updateReservation);

                reservationProducer.produce(afterSaving.getResId().toString());
                break;
            }
            default: {
                throw new RuntimeException("Can't update, it has status: " + reservationEntity.getBookingState());
            }
        }

        return reservationUpdateData.getResId();
    }

    public Integer cancelReservation(Integer resId) {
        ReservationEntity reservationEntity = this.reservationRepository.findByResId(resId);

        if (reservationEntity == null) {
            throw new RuntimeException("ReservationId: " + resId + " not found");
        }

        if (reservationEntity.getBookingState() == BookingState.BOOKED) {
            if (islandBookingService.cancelReservation(reservationEntity)) {
                return resId;
            } else {
                throw new RuntimeException("Failed to cancel reservation, something went wrong while canceling!");
            }

        } else {
            throw new RuntimeException("Failed to cancel reservation, since its not in BOOKED state,"
                    + " currently holds state: " + reservationEntity.getBookingState());
        }
    }


}
