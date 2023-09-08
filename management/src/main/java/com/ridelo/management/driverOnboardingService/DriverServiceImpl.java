package com.ridelo.management.driverOnboardingService;

import com.ridelo.management.databaseService.repository.DriverRepository;
import com.ridelo.management.entities.Driver;
import com.ridelo.management.model.EmailInfo;
import com.ridelo.management.notificationService.EmailDataConstants;
import com.ridelo.management.notificationService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private DriverRepository driverRepository;


    @Override
    public Driver updateRegistration(Driver driver){
        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> getListOfAvailableDrivers(){
        return driverRepository.findAllActiveDrivers();
    }

    @Override
    public List<Driver> getListOfAllInactiveDrivers(){
        return driverRepository.findAllActiveDrivers();
    }

    @Override
    public List<Driver> getDriversWhoseDocumentVerificationIsIncomplete(){
        return driverRepository.getDriversWhoseDocumentVerificationIsIncomplete();
    }

    @Override
    public Driver getDriverById(UUID driverId){
        Optional<Driver> driver =  driverRepository.findById(driverId);
        return driver.orElse(null);
    }

    /**
     * This is used for driver registration.
     * @param driver driver registration request object
     * @return driver registration object
     * @throws MessagingException email exception
     * @throws UnsupportedEncodingException email exception.
     */
    @Override
    public Driver registration(Driver driver) throws MessagingException, UnsupportedEncodingException {
         Driver rDriver = driverRepository.save(Driver.builder().dateOfBirth(driver.getDateOfBirth())
                .name(driver.getName())
                .email(driver.getEmail())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .contact(driver.getContact())
                 .driverIsActive(true).build());
         EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                (EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString());
         emailInfo.setBody(String.format(emailInfo.getBody(),rDriver.getName(),
                rDriver.getDriverId().toString()));
         emailService.sendEmail(EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString()
                ,rDriver.getEmail(),emailInfo);
         return rDriver;
    }

    /**
     * Used to update the driver profile.
     * @param driver driver information to update
     * @return response object.
     * @throws MessagingException handle email exception
     * @throws UnsupportedEncodingException handle email exception
     */
    @Override
    public ResponseEntity<Object> updateProfile(Driver driver) throws MessagingException, UnsupportedEncodingException {
        Driver oldRecord = driverRepository.findById(driver.getDriverId()).orElse(null);
        //todo: verify me
        if(oldRecord!=null){
            if(driver.getName()!=null && !driver.getName().equals("")){
               oldRecord.setName(driver.getName());
            }
            if(driver.getContact()!=null && !driver.getContact().equals("")){
                oldRecord.setContact(driver.getContact());
            }
            if(driver.getAddress()!=null && !driver.getAddress().equals("")){
                oldRecord.setAddress(driver.getAddress());
            }
                if(driver.getEmail()!=null && !driver.getContact().equals("") && !oldRecord.getEmail().equals(driver.getEmail())){
                oldRecord.setEmail(driver.getEmail());
                EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                        (EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString());
                emailInfo.setBody(String.format(emailInfo.getBody(),driver.getName(),
                        driver.getDriverId()));
                emailService.sendEmail(EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString()
                        ,driver.getEmail(),emailInfo);
                oldRecord.setEmailVerified(false);
            }
            driverRepository.save(oldRecord);
            EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                    (EmailDataConstants.MessageType.PROFILE_UPDATE.toString());
            emailInfo.setBody(String.format(emailInfo.getBody(),oldRecord.getName(),
                    oldRecord));
            emailService.sendEmail(EmailDataConstants.MessageType.PROFILE_UPDATE.toString(),
                    oldRecord.getEmail(),emailInfo);
        }
        return new ResponseEntity<>(oldRecord,HttpStatus.ACCEPTED);
    }

    /**
     * USed to update the driver availability status. Driver cannot update the
     * status to true, unless the document verification is complete and driver account
     * status is true(ACTIVE).
     * @param driverId driver id
     * @return response object.
     */
    @Override
    public ResponseEntity<Object> updateAvailabilityStatus(UUID driverId){
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver==null) return new ResponseEntity<>("Driver id is invalid..", HttpStatus.BAD_REQUEST);
        if(!driver.isDriverIsActive()) {
            return new ResponseEntity<>("Driver is not inactive anymore. " +
                    "Please reach out to system admin to set active again.",
                    HttpStatus.ACCEPTED);
        }
        if(!driver.isDocumentVerificationStatus()){
            return new ResponseEntity<>("Driver document verification is not complete yet, " +
                    "you need to wait for the verification to complete before setting yourself active.",
                    HttpStatus.ACCEPTED);
        }
        driver.setDriverAvailabilityStatus(!driver.isDriverAvailabilityStatus());
        driverRepository.save(driver);
        return new ResponseEntity<>("Your availability status is set to "+
                driver.isDriverAvailabilityStatus(),HttpStatus.ACCEPTED);
    }

    /**
     * THis method is used for email verification.
     * @param driverId driver Id
     * @return response entity.
     */
    @Override
    public ResponseEntity<Object> emailVerification(UUID driverId){
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver!=null){
            driver.setEmailVerified(true);
            driverRepository.save(driver);
            return new ResponseEntity<>("Email verified successfully for "+driver.getName(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("The Verification Link is invalid",HttpStatus.NOT_FOUND);
    }

    @Override
    public void deleteDriverById(UUID driverId) {
        driverRepository.deleteById(driverId);
    }

}
