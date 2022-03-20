package io.github.rusyasoft.upgrade.volcano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
public class IslandBookingApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC")); // TO AVOID any zone issues, just going with UTC time
        SpringApplication.run(IslandBookingApplication.class, args);
    }
}
