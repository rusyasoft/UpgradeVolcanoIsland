package io.github.rusyasoft.upgrade.volcano.repository;

import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;
import io.github.rusyasoft.upgrade.volcano.model.ReserveDateEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ReserveDateRepository extends CrudRepository<ReserveDateEntity, Integer> {
    List<ReserveDateEntity> findByResId(Integer resId);

    List<ReserveDateEntity> findAllBySingleDateBetween(
            Date singleDateStart,
            Date singleDateEnd
    );

//    @Query(value = "SELECT id, singleDate, resId FROM ReserveDate where where singleDate >= ?1 and singleDate <= ?2 " +
//            "and resId is null",
//            nativeQuery = true)
    List<ReserveDateEntity> findAllBySingleDateBetweenAndResIdIsNull(
            Date singleDateStart,
            Date singleDateEnd
    );

    @Modifying
    @Query(value = "update reservedate set resId = ?1 where singleDate >= ?2 and singleDate <= ?3 ",
            nativeQuery = true)
    void updateDatesForResId(int resId, Date singleDateStart, Date singleDateEnd);

    @Modifying
    @Query(value = "update reservedate set resId = null where singleDate >= ?1 and singleDate <= ?2 ",
            nativeQuery = true)
    void cancelDatesForResId(Date singleDateStart, Date singleDateEnd);

}
