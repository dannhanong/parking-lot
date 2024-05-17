package com.hotrodoan.service;

import com.hotrodoan.model.Block;
import com.hotrodoan.model.ParkingSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParkingSlotService {
    Page<ParkingSlot> getAllParkingSlot(Pageable pageable);
    ParkingSlot addParkingSlot(ParkingSlot parkingSlot);
    ParkingSlot updateParkingSlot(ParkingSlot parkingSlot, Long id);
    void deleteParkingSlot(Long id);

    List<ParkingSlot> getParkingSlotByBlock(Block block);
    ParkingSlot getParkingSlot(Long id);
    int countUsedParkingSlots(Long id);
    List<ParkingSlot> getAllParkingSlots();
    void updateParkingSlots(List<ParkingSlot> parkingSlots);
    int findMaxSlotNumber();
}
