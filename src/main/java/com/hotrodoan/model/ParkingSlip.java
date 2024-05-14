package com.hotrodoan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "parking_slot_reservation_id")
    private ParkingSlotReservation parkingSlotReservation;

    private Timestamp actualEntryTime;
    private Timestamp actualExitTime;
    private int basicCost;
    private int penalty=0;
    private int totalCost;
    private boolean paid;
}
