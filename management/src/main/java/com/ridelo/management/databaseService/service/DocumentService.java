package com.ridelo.management.databaseService.service;

import com.amazonaws.services.s3.model.S3Object;
import com.ridelo.management.entities.Documents;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public interface DocumentService {
    public Documents upload(MultipartFile file, UUID driverId, String fileType) throws IOException;
    public S3Object download(UUID id);
    public List<Documents> driverDocuments(UUID driverId);
    public ResponseEntity<Object> documentVerificationStatusUpdate(UUID driverId)
            throws MessagingException, UnsupportedEncodingException;
}
