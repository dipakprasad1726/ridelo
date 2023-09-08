package com.ridelo.management.driverOnboardingService;

import com.ridelo.management.ManagementApplication;
import com.ridelo.management.databaseService.service.DocumentService;
import com.ridelo.management.entities.Driver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@SpringBootTest(classes = ManagementApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DriverServiceTest {

    private static UUID driverId;
    @Autowired
    DriverService driverService;

    @Autowired
    DocumentService documentService;

    @Test
    @Order(1)
    public void testDriverRegistration() throws MessagingException, UnsupportedEncodingException {
        Driver driver = Driver.builder()
                .name("Test Driver")
                .email("testemail@gamail.com")
                .address("test lane 420..")
                .contact("987654321")
                .build();
        Driver regDriver = driverService.registration(driver);
        Driver d = driverService.getDriverById(regDriver.getDriverId());
        Assertions.assertNotEquals(d, null);
        driverId = regDriver.getDriverId();
    }

    @Test
    @Order(2)
    public void testEmailVerification(){
        Driver driver = driverService.getDriverById(driverId);
        Assertions.assertEquals(driver.isEmailVerified(),false);
        driverService.emailVerification(driverId);
        driver = driverService.getDriverById(driverId);
        Assertions.assertEquals(driver.isEmailVerified(),true);
    }

//    @Test
//    @Order(3)
//    public void testDocumentVerification() throws MessagingException, UnsupportedEncodingException {
//        Driver driver = driverService.getDriverById(driverId);
//        Assertions.assertEquals(driver.isDocumentVerificationStatus(),false);
//        documentService.documentVerificationStatusUpdate(driver.getDriverId());
//        driver = driverService.getDriverById(driverId);
//        Assertions.assertEquals(driver.isDocumentVerificationStatus(),true);
//    }
//
//    @Test
//    @Order(4)
//    public void updateDriverAvailabilityStatus(){
//        Driver driver = driverService.getDriverById(driverId);
//        Assertions.assertEquals(driver.isDriverAvailabilityStatus(),false);
//        driverService.updateAvailabilityStatus(driver.getDriverId());
//        driver = driverService.getDriverById(driverId);
//        Assertions.assertEquals(driver.isDriverAvailabilityStatus(),true);
//    }
//
//    @Test
//    @Order(5)
//    public void updateDriverProfile() throws MessagingException, UnsupportedEncodingException {
//        Driver driver = driverService.getDriverById(driverId);
//        Driver driver1 = Driver.builder()
//                .driverId(driverId)
//                .address("new address updated")
//                .name("Dummy Name")
//                .build();
//
//        Assertions.assertNotEquals(driver.getAddress(),driver1.getAddress());
//        Assertions.assertNotEquals(driver.getName(),driver1.getName());
//        driverService.updateProfile(driver1);
//        driver = driverService.getDriverById(driverId);
//        Assertions.assertEquals(driver.getAddress(),driver1.getAddress());
//        Assertions.assertEquals(driver.getName(),driver1.getName());
//    }


}
