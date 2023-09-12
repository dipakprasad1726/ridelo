package com.ridelo.management.databaseService.service;

import com.amazonaws.services.glue.model.EntityNotFoundException;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.ridelo.management.amazon.AmazonS3Service;
import com.ridelo.management.common.SuccessMessageConstants;
import com.ridelo.management.databaseService.repository.DocumentsRepository;
import com.ridelo.management.databaseService.repository.DriverRepository;
import com.ridelo.management.entities.Documents;
import com.ridelo.management.entities.Driver;
import com.ridelo.management.globalException.InvalidDriverIdException;
import com.ridelo.management.model.EmailInfo;
import com.ridelo.management.notificationService.EmailDataConstants;
import com.ridelo.management.notificationService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    DocumentsRepository documentsRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    private EmailService emailService;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;


    @Override
    public Documents upload(MultipartFile file, UUID driverId, String fileType) throws IOException {
        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s/%s", bucketName,
                driverId,
                fileType);
        String fileName = String.format("%s", file.getOriginalFilename());

        // Uploading file to s3
        PutObjectResult putObjectResult = amazonS3Service.upload(
                path, fileName, Optional.of(metadata), file.getInputStream());

        // Saving metadata to db
        return documentsRepository.save(Documents.builder()
                .documentType(fileType)
                .verificationStatus(false)
                .fileName(fileName)
                .filePath(path)
                .driverData(driverRepository.findById(driverId).orElse(new Driver())).build());

    }

    @Override
    public S3Object download(UUID id) {
        Documents fileMeta = documentsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("The required document is not available"));
        return amazonS3Service.download(fileMeta.getFilePath(),fileMeta.getFileName());
    }

    @Override
    public List<Documents> driverDocuments(UUID driverId) {
        Driver driverData = driverRepository.findById(driverId).orElseThrow(()->
                new EntityNotFoundException("The required document is not available"));
        return driverData.getDocumentsList();
    }

    @Override
    public ResponseEntity<Object> documentVerificationStatusUpdate(UUID driverId) throws MessagingException, UnsupportedEncodingException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(()->new InvalidDriverIdException(driverId));
        driver.setDocumentVerificationStatus(!driver.isDocumentVerificationStatus());
        driverRepository.save(driver);
        List<Documents> documentsList = driver.getDocumentsList();
        //For simplicity updating the status of all the documents together.
        for (Documents doc:documentsList) {
            doc.setVerificationStatus(!doc.isVerificationStatus());
            documentsRepository.save(doc);
        }
        EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                (EmailDataConstants.MessageType.DOCUMENT_VERIFICATION.toString());
        emailInfo.setBody(String.format(emailInfo.getBody(),driver.getName(),
                driver.isDocumentVerificationStatus(),driver.getAddress()));
        emailService.sendEmail(EmailDataConstants.MessageType.DOCUMENT_VERIFICATION.toString()
                ,driver.getEmail(),emailInfo);
        return new ResponseEntity<>(SuccessMessageConstants.DOCUMENT_VERIFICATION_RESPONSE+
                driver.getName()+" to "+
                driver.isDocumentVerificationStatus(), HttpStatus.ACCEPTED);
    }
}
