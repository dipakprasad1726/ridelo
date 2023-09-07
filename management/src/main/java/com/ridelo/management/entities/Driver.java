package com.ridelo.management.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "driver")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID driverId;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String contact;
    private String dateOfBirth;
    private boolean isEmailVerified=false;
    private boolean documentVerificationStatus = false;
    private boolean driverAvailabilityStatus = false;
    private boolean driverIsActive = true;
    private long createdAt;
    private long updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "driverData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Documents> documentsList;

}

