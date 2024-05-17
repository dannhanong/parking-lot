package com.hotrodoan.service.impl;

import com.hotrodoan.model.ParkingSlip;
import com.hotrodoan.model.ParkingSlotReservation;
import com.hotrodoan.repository.ParkingSlipRepository;
import com.hotrodoan.service.ParkingSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ParkingSlipServiceImpl implements ParkingSlipService {
    @Autowired
    private ParkingSlipRepository parkingSlipRepository;

    @Override
    public Page<ParkingSlip> getAllParkingSlip(Pageable pageable) {
        return parkingSlipRepository.findAll(pageable);
    }

    @Override
    public ParkingSlip createParkingSlip(ParkingSlip parkingSlip) {
        return parkingSlipRepository.save(parkingSlip);
    }

    @Override
    public ParkingSlip updateParkingSlip(ParkingSlip parkingSlip, Long id) {
        return parkingSlipRepository.findById(id).map(ps -> {
            ps.setParkingSlotReservation(parkingSlip.getParkingSlotReservation());
            ps.setActualEntryTime(parkingSlip.getActualEntryTime());
            ps.setActualExitTime(parkingSlip.getActualExitTime());
            ps.setBasicCost(parkingSlip.getBasicCost());
//            ps.setPenalty(parkingSlip.getPenalty());
            ps.setTotalCost(parkingSlip.getTotalCost());
            ps.setPaid(parkingSlip.isPaid());
            return parkingSlipRepository.save(ps);
        }).orElseThrow(() -> new RuntimeException("Not found parking slip"));
    }

    @Override
    public void deleteParkingSlip(Long id) {
        parkingSlipRepository.deleteById(id);
    }

    @Override
    public ParkingSlip getParkingSlip(Long id) {
        return parkingSlipRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found parking slip"));
    }

    @Override
    public ParkingSlip getParkingSlipByParkingSlotReservation(ParkingSlotReservation parkingSlotReservation) {
        return parkingSlipRepository.findByParkingSlotReservation(parkingSlotReservation);
    }
}
