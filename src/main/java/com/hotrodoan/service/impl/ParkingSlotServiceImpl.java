package com.hotrodoan.service.impl;

import com.hotrodoan.model.ParkingSlot;
import com.hotrodoan.service.ParkingSlotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ParkingSlotServiceImpl implements ParkingSlotService {
    @Override
    public Page<ParkingSlot> getAllParkingSlot(Pageable pageable) {
        return null;
    }

    @Override
    public ParkingSlot addParkingSlot(ParkingSlot parkingSlot) {
        return null;
    }

    @Override
    public ParkingSlot updateParkingSlot(ParkingSlot parkingSlot, Long id) {
        return null;
    }

    @Override
    public void deleteParkingSlot(Long id) {

    }
}
