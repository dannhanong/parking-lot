package com.hotrodoan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ParkingLot parkingLot;

    @NotBlank(message = "Block code is required")
    @Size(min = 1, max = 60)
    private String blockCode;
    private int numberOfParkingSlots = 0;
    @Column(nullable = true)
    private boolean blockFull = false;
    private int usedParkingSlots = 0;
}
