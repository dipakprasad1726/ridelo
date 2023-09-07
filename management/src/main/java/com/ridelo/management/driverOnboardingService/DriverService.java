package com.ridelo.management.driverOnboardingService;

import com.ridelo.management.entities.Driver;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public interface DriverService {
    Driver updateRegistration(Driver driver);

    List<Driver> getListOfAvailableDrivers();

    List<Driver> getListOfAllInactiveDrivers();

    List<Driver> getDriversWhoseDocumentVerificationIsIncomplete();

    Driver getDriverById(UUID driverId);

    Driver registration(Driver driver) throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<Object> updateProfile(Driver driver) throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<Object> updateAvailabilityStatus(UUID driverId);

    ResponseEntity<Object> emailVerification(UUID driverId);
}
