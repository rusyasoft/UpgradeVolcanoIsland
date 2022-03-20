package io.github.rusyasoft.upgrade.volcano.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Contact {
    String firstName;
    String lastName;
    String email;
}
