package io.github.rusyasoft.upgrade.volcano.tools;

import javafx.util.Pair;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReservationValidation {

    public static final int MAX_ALLOWED_DAYS_TO_RESERVE = 3;
    public static final int MAX_ALLOWED_DAYS_TO_RESERVE_AHEAD = 30;

    public static Pair<Date, Date> validatedAvailableDates(Date fromDate, Date toDate) {
        // if one of the dates are empty then we go with current date and +3 date interval
        if (fromDate == null || toDate == null) {
            Calendar calendar = Calendar.getInstance();
            Date todayDate = DateUtils.getToday();
            calendar.setTime(todayDate);
            calendar.add(Calendar.DATE, 30); // by default 1 month is checked
            Date todayPlus30 = calendar.getTime();

            return new Pair<>(todayDate, todayPlus30);
        }

        // TODO: perform all other necessary checks

        return new Pair<>(fromDate, toDate);
    }

    public static void validateNewReservationDatesInterval(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            throw new RuntimeException("Reservation Period is given wrong!");
        }

        if (toDate.before(fromDate)) {
            throw new RuntimeException("interval is given wrong!");
        }

        Date today = DateUtils.getToday();
        if (toDate.before(today)) {
            throw new RuntimeException("cannot do reservation for passed dates!");
        }

        long intervalInDays = getDiffInDays(fromDate, toDate);
        if (intervalInDays > MAX_ALLOWED_DAYS_TO_RESERVE) {
            throw new RuntimeException("interval to reserve can be maximum: " + MAX_ALLOWED_DAYS_TO_RESERVE);
        }

        // minimum 1 day ahead of arrival and up to 1 month in advance
        long daysFromTodayToStartDate = getDiffInDays(today, fromDate);
        if (daysFromTodayToStartDate < 1 || daysFromTodayToStartDate > MAX_ALLOWED_DAYS_TO_RESERVE_AHEAD) {
            throw new RuntimeException("reservation can be done " + MAX_ALLOWED_DAYS_TO_RESERVE_AHEAD + " days ahead");
        }
    }

    private static long getDiffInDays(Date fromDate, Date toDate) {
        long diffInMillis = Math.abs(toDate.getTime() - fromDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1; // inclusive dates
    }


}
