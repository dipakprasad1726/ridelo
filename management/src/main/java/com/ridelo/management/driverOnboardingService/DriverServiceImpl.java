package com.ridelo.management.driverOnboardingService;

import com.ridelo.management.common.ExceptionConstants;
import com.ridelo.management.common.SuccessMessageConstants;
import com.ridelo.management.databaseService.repository.DriverRepository;
import com.ridelo.management.entities.Driver;
import com.ridelo.management.globalException.*;
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
import java.util.UUID;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private DriverRepository driverRepository;


    /**
     * Get the list of available drivers
     * @return list of available drivers
     */
    @Override
    public List<Driver> getListOfAvailableDrivers(){
        return driverRepository.findAllActiveDrivers();
    }

    /**
     * Get list of all inactive drivers
     * @return list of all inactive drivers
     */
    @Override
    public List<Driver> getListOfAllInactiveDrivers(){
        return driverRepository.findAllActiveDrivers();
    }

    /**
     * Get list of drivers whose document verification is incomplete
     * @return list of drivers
     */
    @Override
    public List<Driver> getDriversWhoseDocumentVerificationIsIncomplete(){
        return driverRepository.getDriversWhoseDocumentVerificationIsIncomplete();
    }

    /**
     * Get driver by Id
     * @param driverId driver id
     * @return driver object
     */
    @Override
    public Driver getDriverById(UUID driverId){
        Driver driver =  driverRepository.findById(driverId).orElseThrow(()->new InvalidDriverIdException(driverId));;
        return driver;
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
        if(driver.getName() == null || driver.getName().equals("")){
            throw new InvalidDriverDataException(ExceptionConstants.NAME_CANNOT_BE_EMPTY);
        }
        if(driver.getEmail() == null || driver.getEmail().equals("")){
            throw new InvalidDriverDataException(ExceptionConstants.EMAIL_CANNOT_BE_EMPTY);
        }
        if(driver.getAddress() == null || driver.getAddress().equals("")){
            throw new InvalidDriverDataException(ExceptionConstants.ADDRESS_CANNOT_BE_EMPTY);
        }
        if(verifyEmailValidity(driver.getEmail())){
            throw new InvalidDriverDataException(ExceptionConstants.EMAIL_ALREADY_EXIST);
        }
        Driver rDriver = driverRepository.save(Driver.builder().dateOfBirth(driver.getDateOfBirth())
                .name(driver.getName())
                .email(driver.getEmail())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .contact(driver.getContact())
                .address(driver.getAddress())
                .driverIsActive(true).build());
         EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                (EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString());
         emailInfo.setBody(String.format(emailInfo.getBody(),rDriver.getName(),
                rDriver.getDriverId().toString()));
         emailService.sendEmail(EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString()
                ,rDriver.getEmail(),emailInfo);
         return rDriver;
    }

    private boolean verifyEmailValidity(String email){
        return driverRepository.getEmailCount(email) > 0;
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
        Driver oldRecord = driverRepository.findById(driver.getDriverId()).orElseThrow(()->new InvalidDriverIdException(driver.getDriverId()));
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
        driver.setUpdatedAt(System.currentTimeMillis());
        driverRepository.save(oldRecord);
        EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                (EmailDataConstants.MessageType.PROFILE_UPDATE.toString());
        emailInfo.setBody(String.format(emailInfo.getBody(),oldRecord.getName(),
                EmailDataConstants.buildDriverData(oldRecord)));
        emailService.sendEmail(EmailDataConstants.MessageType.PROFILE_UPDATE.toString(),
                oldRecord.getEmail(),emailInfo);
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
        Driver driver = driverRepository.findById(driverId).orElseThrow(()-> new InvalidDriverIdException(driverId));
        if(!driver.isDriverIsActive()) {
            throw new InactiveDriverException();
        }
        if(!driver.isDocumentVerificationStatus()){
            throw new DocumentVerificationException();
        }
        driver.setDriverAvailabilityStatus(!driver.isDriverAvailabilityStatus());
        driverRepository.save(driver);
        return new ResponseEntity<>(SuccessMessageConstants.DRIVER_AVAILABILITY_STATUS_MESSAGE +
                driver.isDriverAvailabilityStatus(),HttpStatus.ACCEPTED);
    }

    /**
     * THis method is used for email verification.
     * @param driverId driver Id
     * @return response entity.
     */
    @Override
    public ResponseEntity<Object> emailVerification(UUID driverId){
        try {
            Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new InvalidDriverIdException(driverId));
            driver.setEmailVerified(true);
            driverRepository.save(driver);
            return new ResponseEntity<>(SuccessMessageConstants.EMAIL_VERIFICATION_MESSAGE + driver.getName(), HttpStatus.ACCEPTED);
        }catch (Exception e){
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Override
    public void deleteDriverById(UUID driverId) {
        driverRepository.deleteById(driverId);
    }

}
