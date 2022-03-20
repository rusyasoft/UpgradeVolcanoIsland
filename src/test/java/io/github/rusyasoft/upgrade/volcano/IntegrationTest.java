package io.github.rusyasoft.upgrade.volcano;

import io.github.rusyasoft.upgrade.volcano.controller.ReservationController;
import io.github.rusyasoft.upgrade.volcano.model.BookingState;
import io.github.rusyasoft.upgrade.volcano.model.Contact;
import io.github.rusyasoft.upgrade.volcano.model.ContactAndDates;
import io.github.rusyasoft.upgrade.volcano.model.ReservationEntity;
import io.github.rusyasoft.upgrade.volcano.tools.DateUtils;
import io.github.rusyasoft.upgrade.volcano.tools.ReservationValidation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class IntegrationTest {
    @Autowired
    ReservationController reservationController;

    final SimpleDateFormat readDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void successfulReservation() throws ParseException, InterruptedException {

        Contact contact = Contact.builder()
                .firstName("Rustam")
                .lastName("Rakhimov")
                .email("rusyasoft@gmail.com")
                .build();
        ContactAndDates contactAndDates = ContactAndDates.builder()
                .contact(contact)
                .fromDate(readDateFormat.parse("2022-03-24"))
                .toDate(readDateFormat.parse("2022-03-26"))
                .build();

        try (MockedStatic<DateUtils> mockedStatic = Mockito.mockStatic(DateUtils.class)) {
            mockedStatic.when(DateUtils::getToday).thenReturn(readDateFormat.parse("2022-03-03"));
            int resId = reservationController.createReservation(contactAndDates);
            TimeUnit.SECONDS.sleep(3);

            ReservationEntity reservationEntity = reservationController.getReservationStatus(resId);
            Assert.assertEquals(BookingState.BOOKED, reservationEntity.getBookingState());
        }






    }
}
