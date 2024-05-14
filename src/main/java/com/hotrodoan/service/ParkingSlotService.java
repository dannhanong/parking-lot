package com.hotrodoan.service;

import com.hotrodoan.model.ParkingSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingSlotService {
    Page<ParkingSlot> getAllParkingSlot(Pageable pageable);
    ParkingSlot addParkingSlot(ParkingSlot parkingSlot);
    ParkingSlot updateParkingSlot(ParkingSlot parkingSlot, Long id);
    void deleteParkingSlot(Long id);
}
