package io.github.rusyasoft.upgrade.volcano.tools;

import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationValidationTest {

    private Date todayDate;
    private Date todayPlus3;
    private Date todayPlus10;
    private Date todayPlus30;

    public ReservationValidationTest() {
        Calendar calendar = Calendar.getInstance();
        this.todayDate = new Date();

        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, 2); // 3 days
        this.todayPlus3 = calendar.getTime();

        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, 10); // check 10 days interval
        this.todayPlus10 = calendar.getTime();

        calendar.setTime(this.todayDate);
        calendar.add(Calendar.DATE, 30); // by default 1 month is checked
        this.todayPlus30 = calendar.getTime();
    }

    @Test
    public void validatedAvailableDatesNoDates() {
        Pair<Date, Date> interval = ReservationValidation.validatedAvailableDates(null, null);

        Assert.assertEquals(extractDate(todayDate), extractDate(interval.getKey()));
        Assert.assertEquals(extractDate(todayPlus30), extractDate(interval.getValue()));
    }

    @Test
    public void validatedAvailableProperInterval() {
        Pair<Date, Date> interval = ReservationValidation.validatedAvailableDates(todayDate, todayPlus10);

        Assert.assertEquals(extractDate(todayDate), extractDate(interval.getKey()));
        Assert.assertEquals(extractDate(todayPlus10), extractDate(interval.getValue()));
    }

    @Test
    public void validateNewReservationDatesIntervalSucceed() {
        ReservationValidation.validateNewReservationDatesInterval(todayDate, todayPlus3);
    }

    @Test(expected = RuntimeException.class)
    public void validateNewReservationDatesIntervalException() {
        ReservationValidation.validateNewReservationDatesInterval(todayPlus3, todayDate);
        Assert.fail();
    }

    @Test(expected = RuntimeException.class)
    public void validateNewReservationDatesOverMaxException() {
        ReservationValidation.validateNewReservationDatesInterval(todayDate, todayPlus10);
        Assert.fail();
    }

    @Test
    public void validateNewReserveMaxDates() {
        ReservationValidation.validateNewReservationDatesInterval(todayDate, todayPlus3);
        Assert.assertTrue(true);
    }

    @Test(expected = RuntimeException.class)
    public void validateNewReserveEmptyDatesException() {
        ReservationValidation.validateNewReservationDatesInterval(null, null);
        Assert.fail();
    }

    private String extractDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
