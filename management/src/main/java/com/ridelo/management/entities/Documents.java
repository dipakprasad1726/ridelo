package com.ridelo.management.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "documents")
public class Documents {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID documentId;
    private String documentType;
    private String fileName;
    private String filePath;
    private boolean verificationStatus;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private Driver driverData;
}
