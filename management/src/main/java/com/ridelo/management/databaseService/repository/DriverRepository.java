package com.ridelo.management.databaseService.repository;

import com.ridelo.management.entities.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface DriverRepository extends CrudRepository<Driver, UUID> {

    @Query(value = "select * from driver where document_verification_status=true and driver_availability_status=true and driver_is_active=true", nativeQuery = true)
    List<Driver> findAllActiveDrivers();

    @Query(value = "select * from driver where document_verification_status=true and " +
            "driver_availability_status=false and driver_is_active=true" , nativeQuery = true)
    List<Driver> findAllInactiveDrivers();

    @Query(value = "select * from driver where document_verification_status=false", nativeQuery = true)
    List<Driver> getDriversWhoseDocumentVerificationIsIncomplete();

    @Query(value = "select count(*) from driver where email = ?1 ",nativeQuery = true)
    int getEmailCount(@Param("email") String email);

}
