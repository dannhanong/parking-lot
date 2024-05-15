package com.hotrodoan.service.impl;

import com.hotrodoan.model.*;
import com.hotrodoan.repository.ParkingSlotReservationRepository;
import com.hotrodoan.service.ParkingSlotReservationService;
import com.hotrodoan.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSlotReservationServiceImpl implements ParkingSlotReservationService {
    @Autowired
    private ParkingSlotReservationRepository parkingSlotReservationRepository;
    @Autowired
    private ParkingSlotService parkingSlotService;
    

    @Override
    public ParkingSlotReservation createParkingSlotReservation(ParkingSlotReservation parkingSlotReservation) {
        parkingSlotReservation.setBookingDate(new Date(System.currentTimeMillis()));
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

    @Override
    public List<ParkingSlotReservation> getParkingSlotReservationsByParkingSlot(ParkingSlot parkingSlot) {
        return parkingSlotReservationRepository.findByParkingSlot(parkingSlot);
    }

    @Override
    public List<ParkingSlot> findAvailableParkingSlots(Timestamp startTimestamp, int durationInMinutes, Block block, ParkingLot parkingLot) {
        List<ParkingSlot> allParkingSlots = parkingSlotService.getAllParkingSlots();
        LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();
        // Lọc ra những ParkingSlot không có ParkingSlotReservation nào trùng thời gian
        List<ParkingSlot> availableParkingSlots = allParkingSlots.stream()
                .filter(parkingSlot -> parkingSlot.getBlock().equals(block) && parkingSlot.getBlock().getParkingLot().equals(parkingLot))
                .filter(parkingSlot -> {
                    List<ParkingSlotReservation> reservations = parkingSlotReservationRepository.findByParkingSlot(parkingSlot);
                    return reservations.stream()
                            .noneMatch(reservation -> {
                                LocalDateTime reservationEnd = reservation.getStartTimestamp().toLocalDateTime().plusMinutes(reservation.getDurationInMinutes());
                                LocalDateTime desiredEnd = startLocalDateTime.plusMinutes(durationInMinutes);
                                return startLocalDateTime.isBefore(reservationEnd) && desiredEnd.isAfter(reservation.getStartTimestamp().toLocalDateTime());
                            });
                })
                .collect(Collectors.toList());
        return availableParkingSlots;
    }

    @Override
    public List<ParkingSlotReservation> findPastReservations() {
        List<ParkingSlotReservation> allReservations = parkingSlotReservationRepository.findAll();

        // Lọc ra những đặt chỗ đã kết thúc
        List<ParkingSlotReservation> pastReservations = allReservations.stream()
                .filter(reservation -> reservation.getStartTimestamp().toInstant()
                        .plusMillis(reservation.getDurationInMinutes() * 60 * 1000)
                        .isBefore(Instant.now()))
                .collect(Collectors.toList());

        return pastReservations;
    }
}
