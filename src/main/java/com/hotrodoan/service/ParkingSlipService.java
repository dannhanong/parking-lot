package com.hotrodoan.service;

import com.hotrodoan.model.ParkingSlip;
import com.hotrodoan.model.ParkingSlotReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingSlipService {
    Page<ParkingSlip> getAllParkingSlip(Pageable pageable);
    ParkingSlip createParkingSlip(ParkingSlip parkingSlip);
    ParkingSlip updateParkingSlip(ParkingSlip parkingSlip, Long id);
    void deleteParkingSlip(Long id);
    ParkingSlip getParkingSlip(Long id);
    ParkingSlip getParkingSlipByParkingSlotReservation(ParkingSlotReservation parkingSlotReservation);
}
