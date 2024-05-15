package com.hotrodoan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    private Timestamp startTimestamp;
    private int durationInMinutes;
    private Date bookingDate;

    @ManyToOne
    private ParkingSlot parkingSlot;

    private Timestamp actualEntryTime;
    private Timestamp actualExitTime;
    private int basicCost;
    private int penalty;
    private int totalCost;
    private boolean paid;
}
