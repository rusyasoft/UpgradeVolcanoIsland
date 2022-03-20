package io.github.rusyasoft.upgrade.volcano.tools;

import javafx.util.Pair;

import java.util.Calendar;
import java.util.Date;

public class ReservationValidation {

    public static Pair<Date, Date> validatedAvailableDates(Date fromDate, Date toDate) {
        // if one of the dates are empty then we go with current date and +3 date interval
        if (fromDate == null || toDate == null) {
            Calendar calendar = Calendar.getInstance();
            Date todayDate = new Date();
            calendar.setTime(todayDate);
            calendar.add(Calendar.DATE, 2); // 2 because today date also inclusive
            Date todayPlus3 = calendar.getTime();

            return new Pair<>(todayDate, todayPlus3);
        }

        // TODO: perform all other checkings

        return new Pair<>(fromDate, toDate);
    }

    public static void validateNewReservationDatesInterval(Date fromDate, Date toDate) {
        // TODO: perform all reservation date intervals
        if (toDate.before(fromDate)) {
            throw new RuntimeException("interval is given wrong!");
        }
    }
}
