package com.hotrodoan.service;

import com.hotrodoan.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface ParkingSlotReservationService {
    ParkingSlotReservation createParkingSlotReservation(ParkingSlotReservation parkingSlotReservation);
    Page<ParkingSlotReservation> getAllParkingSlotReservations(Date date, Pageable pageable);
    ParkingSlotReservation updateParkingSlotReservation(ParkingSlotReservation parkingSlotReservation, Long id);
    void deleteParkingSlotReservation(Long id);
    ParkingSlotReservation getParkingSlotReservation(Long id);
    Page<ParkingSlotReservation> getAllParkingSlotReservationsByCustomer(Customer customer, Date date, Pageable pageable);
    List<ParkingSlot> findAvailableParkingSlots(int slotNumber, Date bookingDate, int durationInMinutes);
    List<ParkingSlotReservation> getParkingSlotReservationsByParkingSlot(ParkingSlot parkingSlot);
    List<ParkingSlot> findAvailableParkingSlots(Timestamp startTimestamp, int durationInMinutes, Block block, ParkingLot parkingLot);
}
