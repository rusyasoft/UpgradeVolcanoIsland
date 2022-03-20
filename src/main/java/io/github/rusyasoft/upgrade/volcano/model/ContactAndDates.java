package io.github.rusyasoft.upgrade.volcano.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ContactAndDates {
    Contact contact;
    Date fromDate;
    Date toDate;
}
