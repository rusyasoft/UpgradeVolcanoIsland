package io.github.rusyasoft.upgrade.volcano.controller;

import io.github.rusyasoft.upgrade.volcano.model.ContactAndDates;
import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;
import io.github.rusyasoft.upgrade.volcano.model.ReservationUpdateData;
import io.github.rusyasoft.upgrade.volcano.service.IslandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Api(value = "Reservation", description = "Used to handle reservation related operations")
@RequestMapping("/reservation/")
@RestController
public class ReservationController {

    @Autowired
    private IslandService islandService;

    @GetMapping("/availableDates")
    @ApiOperation(value = "Getting the list of available dates")
    public List<String> getListOfAvailableDates(
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,

            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate
    ) {
        if (fromDate == null || toDate == null) {
            Calendar calendar = Calendar.getInstance();
            Date todayDate = new Date();
            calendar.setTime(todayDate);
            calendar.add(Calendar.DATE, 2); // 2 because today date also inclusive
            Date todayPlus3 = calendar.getTime();

            fromDate = todayDate;
            toDate = todayPlus3;
        }
        return islandService.getListOfAvailableDates(fromDate, toDate);
    }

    @PostMapping(value = "/createReservation")
    @ApiOperation(value = "Creating a new reservation")
    public Integer createReservation(@RequestBody ContactAndDates contactAndDates) {
        return islandService.createReservation(
                contactAndDates.getContact(),
                contactAndDates.getFromDate(),
                contactAndDates.getToDate()
        );
    }

    @GetMapping("/reservationStatus")
    @ApiOperation(value = "Getting the reservation status")
    public ReservationEntity getReservationStatus(
            @RequestParam(name = "resId")
            Integer resId
    ) {
        return islandService.getReservationStatus(resId);
    }

    @PutMapping(value = "/updateReservation")
    @ApiOperation(value = "Updating status of existing reservation")
    public Integer updateReservationStatus(@RequestBody ReservationUpdateData reservationUpdateData) {
        return islandService.updateReservation(reservationUpdateData);
    }

    @PutMapping(value = "/cancelReservation")
    @ApiOperation(value = "Canceling existing reservation")
    public Integer cancelReservation(@RequestParam(name = "resId") Integer resId) {
        return islandService.cancelReservation(resId);
    }

}
