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
    @Column(nullable = true)
    private int basicCost=0;
    @Column(nullable = true)
    private int penalty=0;
    @Column(nullable = true)
    private int totalCost=0;
    @Column(nullable = true)
    private boolean paid=false;
}
