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
    @Min(1)
    private int numberOfBlocks;
    private boolean slotAvailable;
    private String address;
    @Size(min = 5, max = 10)
    private String zip;
    private boolean reentryAllowed;
    private String operatingCompanyName;
    private boolean valetParkingAvailable;
    private int usedSlots = 0;
}
