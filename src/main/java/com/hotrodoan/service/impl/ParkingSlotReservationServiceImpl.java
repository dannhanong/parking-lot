package com.hotrodoan.service.impl;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.ParkingSlot;
import com.hotrodoan.model.ParkingSlotReservation;
import com.hotrodoan.repository.ParkingSlotReservationRepository;
import com.hotrodoan.service.ParkingSlotReservationService;
import com.hotrodoan.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ParkingSlotReservationServiceImpl implements ParkingSlotReservationService {
    @Autowired
    private ParkingSlotReservationRepository parkingSlotReservationRepository;
    @Autowired
    private ParkingSlotService parkingSlotService;

    @Override
    public ParkingSlotReservation createParkingSlotReservation(ParkingSlotReservation parkingSlotReservation) {
        ParkingSlot parkingSlot = parkingSlotReservation.getParkingSlot();
        parkingSlot.setSlotAvailable(false);
        parkingSlotService.updateParkingSlot(parkingSlot, parkingSlot.getId());
        return parkingSlotReservationRepository.save(parkingSlotReservation);
    }

    @Override
    public Page<ParkingSlotReservation> getAllParkingSlotReservations(Date date, Pageable pageable) {
        return parkingSlotReservationRepository.findByBookingDate(date, pageable);
    }

    @Override
    public ParkingSlotReservation updateParkingSlotReservation(ParkingSlotReservation parkingSlotReservation, Long id) {
        return parkingSlotReservationRepository.findById(id).map(ps -> {
            ps.setCustomer(parkingSlotReservation.getCustomer());
            ps.setDurationInMinutes(parkingSlotReservation.getDurationInMinutes());
            ps.setBookingDate(parkingSlotReservation.getBookingDate());
            ps.setParkingSlot(parkingSlotReservation.getParkingSlot());
            return parkingSlotReservationRepository.save(ps);
        }).orElseThrow(() -> new RuntimeException("Not found parking slot reservation"));
    }

    @Override
    public void deleteParkingSlotReservation(Long id) {
        parkingSlotReservationRepository.deleteById(id);
    }

    @Override
    public ParkingSlotReservation getParkingSlotReservation(Long id) {
        return parkingSlotReservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found parking slot reservation"));
    }

    @Override
    public Page<ParkingSlotReservation> getAllParkingSlotReservationsByCustomer(Customer customer, Date date, Pageable pageable) {
        return parkingSlotReservationRepository.findByCustomerAndBookingDate(customer, date, pageable);
    }

    @Override
    public List<ParkingSlot> findAvailableParkingSlots(int slotNumber, Date bookingDate, int durationInMinutes) {
        return null;
    }
}
