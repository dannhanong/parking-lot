package com.hotrodoan.service;

import com.hotrodoan.model.ParkingLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParkingLotService {
    ParkingLot createParkingLot(ParkingLot parkingLot);
    Page<ParkingLot> getAllParkingLots(String keyword, Pageable pageable);
    ParkingLot updateParkingLot(ParkingLot parkingLot, Long id);
    void deleteParkingLot(Long id);
    ParkingLot getParkingLot(Long id);
    List<Object[]> countUsedParkingSlots();
}
