package io.github.rusyasoft.upgrade.volcano.service;

import io.github.rusyasoft.upgrade.volcano.model.BookingState;
import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;
import io.github.rusyasoft.upgrade.volcano.model.ReserveDateEntity;
import io.github.rusyasoft.upgrade.volcano.producer.KafkaProducer;
import io.github.rusyasoft.upgrade.volcano.repository.ReservationRepository;
import io.github.rusyasoft.upgrade.volcano.repository.ReserveDateRepository;
import io.github.rusyasoft.upgrade.volcano.tools.ReservationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class IslandBookingServiceImpl implements IslandBookingService{

    private ReserveDateRepository reserveDateRepository;
    private ReservationRepository reservationRepository;


    @Autowired
    public IslandBookingServiceImpl(ReserveDateRepository reserveDateRepository, ReservationRepository reservationRepository) {
        this.reserveDateRepository = reserveDateRepository;
        this.reservationRepository = reservationRepository;
    }
    @Override
    @Transactional
    public boolean tryToReserve(ReservationEntity reservationEntity) {
        long diffInMillies =
                Math.abs(reservationEntity.getToDate().getTime() - reservationEntity.getFromDate().getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1; // inclusive dates


        List<ReserveDateEntity> aboutToReserveThisInterval =
                this.reserveDateRepository.findAllBySingleDateBetweenAndResIdIsNull(
                        reservationEntity.getFromDate(),
                        reservationEntity.getToDate()
                );
        if (!aboutToReserveThisInterval.isEmpty() && aboutToReserveThisInterval.size() == diffInDays) {
            this.reserveDateRepository.updateDatesForResId(
                    reservationEntity.getResId(),
                    reservationEntity.getFromDate(),
                    reservationEntity.getToDate()
            );
        } else {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean cancelReservation(ReservationEntity reservationEntity) {
        this.reserveDateRepository.cancelDatesForResId(
                reservationEntity.getFromDate(),
                reservationEntity.getToDate()
        );

        ReservationEntity cancelReservationEntity = ReservationBuilder.updatedReservationEntity(reservationEntity,
                BookingState.CANCELED);

        this.reservationRepository.save(cancelReservationEntity);

        return true;
    }
}
