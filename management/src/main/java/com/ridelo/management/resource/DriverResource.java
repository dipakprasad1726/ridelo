package com.ridelo.management.resource;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ridelo.management.databaseService.service.DocumentService;
import com.ridelo.management.driverOnboardingService.DriverService;
import com.ridelo.management.entities.Documents;
import com.ridelo.management.entities.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/driver")
public class DriverResource {

    @Autowired
    DocumentService documentService;

    @Autowired
    DriverService driverService;

    @PostMapping("/registration")
    public Driver driverRegistration(@RequestBody Driver driver) throws MessagingException, UnsupportedEncodingException {
        return driverService.registration(driver);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Object> driverInfo(@PathVariable String id){
        Driver driver = driverService.getDriverById(UUID.fromString(id));
        if(driver==null){
            return new ResponseEntity<>("Invalid driver id...", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(driver,HttpStatus.ACCEPTED);
    }

    @PostMapping("/document/upload")
    public Documents documentUpload(@RequestParam("file") MultipartFile file,@RequestParam String driverId,@RequestParam String fileType) throws IOException {
            return documentService.upload(file, UUID.fromString(driverId), fileType);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable UUID documentId) {
        byte[] content = null;
        S3Object s3Object = documentService.download(documentId);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            s3Object.close();
            log.info("File downloaded successfully");
        } catch(final IOException ex) {
            log.info("IO Error Message= " + ex.getMessage());
        }
        final ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=\"" + documentId + "-document\"")
                .body(resource);
    }

    @PostMapping("/update/availability/{driverId}")
    public ResponseEntity<Object> updateDriverAvailabilityStatus(@PathVariable String driverId){
        return driverService.updateAvailabilityStatus(UUID.fromString(driverId));
    }

    @PostMapping("update/profile")
    public ResponseEntity<Object> updateProfile(@RequestBody Driver driver) throws MessagingException, UnsupportedEncodingException, JSONException {
        return driverService.updateProfile(driver);
    }

    @PostMapping("update/verification/status/{driverId}")
    public ResponseEntity<Object> updateDocumentVerificationStatus(@PathVariable String driverId) throws MessagingException, UnsupportedEncodingException {
        return documentService.documentVerificationStatusUpdate(UUID.fromString(driverId));
    }

    @GetMapping("active")
    public ResponseEntity<Object> getActiveDriversList(){
        return new ResponseEntity<>(driverService.getListOfAvailableDrivers(),HttpStatus.ACCEPTED);
    }

}
