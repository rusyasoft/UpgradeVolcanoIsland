package io.github.rusyasoft.upgrade.volcano.repository;

import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Integer> {
    ReservationEntity findByResId(Integer resId);
}
