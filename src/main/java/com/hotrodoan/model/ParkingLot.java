package com.hotrodoan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Min(1)
    private int numberOfBlocks;
    private boolean slotAvailable;
    private String address;
    @Size(max = 3)
    private String zip="v";
    private boolean reentryAllowed;
    private String operatingCompanyName;
    private boolean valetParkingAvailable;
    @Column(nullable = true)
    private int usedSlots = 0;
}
