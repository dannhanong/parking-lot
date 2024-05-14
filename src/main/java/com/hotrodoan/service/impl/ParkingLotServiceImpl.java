package com.hotrodoan.service.impl;

import com.hotrodoan.model.ParkingLot;
import com.hotrodoan.repository.ParkingLotRepository;
import com.hotrodoan.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Override
    public ParkingLot createParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public Page<ParkingLot> getAllParkingLots(String keyword, Pageable pageable) {
        return parkingLotRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public ParkingLot updateParkingLot(ParkingLot parkingLot, Long id) {
        return parkingLotRepository.findById(id).map(pl -> {
            pl.setNumberOfBlocks(parkingLot.getNumberOfBlocks());
            pl.setSlotAvailable(parkingLot.isSlotAvailable());
            pl.setAddress(parkingLot.getAddress());
            pl.setZip(parkingLot.getZip());
            pl.setReentryAllowed(parkingLot.isReentryAllowed());
            pl.setOperatingCompanyName(parkingLot.getOperatingCompanyName());
            pl.setValetParkingAvailable(parkingLot.isValetParkingAvailable());
            return parkingLotRepository.save(pl);
        }).orElse(null);
    }

    @Override
    public void deleteParkingLot(Long id) {
        parkingLotRepository.deleteById(id);
    }

    @Override
    public ParkingLot getParkingLot(Long id) {
        return parkingLotRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found parking lot"));
    }

   @Override
   public List<Object[]> countUsedParkingSlots() {
       return null;
   }
}
