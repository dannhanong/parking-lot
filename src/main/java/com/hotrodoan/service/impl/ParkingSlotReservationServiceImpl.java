package com.hotrodoan.service.impl;

import com.hotrodoan.model.*;
import com.hotrodoan.model.dto.AvailableParkingSlotsInfo;
import com.hotrodoan.repository.ParkingSlotReservationRepository;
import com.hotrodoan.service.BlockService;
import com.hotrodoan.service.ParkingLotService;
import com.hotrodoan.service.ParkingSlotReservationService;
import com.hotrodoan.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.Ansi8BitColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParkingSlotReservationServiceImpl implements ParkingSlotReservationService {
    @Autowired
    private ParkingSlotReservationRepository parkingSlotReservationRepository;
    @Autowired
    private ParkingSlotService parkingSlotService;
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private BlockService blockService;

    @Override
    public Page<ParkingSlotReservation> getAllParkingSlotReservations(Pageable pageable) {
        return parkingSlotReservationRepository.findAll(pageable);
    }

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
            ps.setCost(parkingSlotReservation.getCost());
            return parkingSlotReservationRepository.save(ps);
        }).orElseThrow(() -> new RuntimeException("Not found parking slot reservation"));
    }

    @Override
    public void deleteParkingSlotReservation(Long id) {
        ParkingSlotReservation parkingSlotReservation = parkingSlotReservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found parking slot reservation"));
        LocalDate bookingDate = parkingSlotReservation.getBookingDate().toLocalDate();
        LocalDate today = LocalDate.now();
        if (bookingDate.isBefore(today)) {
            throw new RuntimeException("Cannot delete past reservation");
        }
        parkingSlotReservationRepository.deleteById(id);
    }

    @Override
    public ParkingSlotReservation getParkingSlotReservation(Long id) {
        return parkingSlotReservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found parking slot reservation"));
    }

    @Override
    public Page<ParkingSlotReservation> getAllParkingSlotReservationsByCustomer(Customer customer, Pageable pageable) {
        return parkingSlotReservationRepository.findByCustomer(customer, pageable);
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
    public List<AvailableParkingSlotsInfo> findAvailableParkingSlotsAndBlockAndParkingLot(Timestamp startTimestamp, int durationInMinutes, Long id) {
        ParkingLot parkingLot = parkingLotService.getParkingLot(id);
        List<Block> allBlocks = blockService.getBlockByParkingLot(parkingLot);
        List<ParkingSlot> allParkingSlots = new ArrayList<>();

        for (Block block : allBlocks) {
            List<ParkingSlot> parkingSlots = parkingSlotService.getParkingSlotByBlock(block);
            allParkingSlots.addAll(parkingSlots);
        }

        LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();

        Map<Block, List<ParkingSlot>> availableSlotsByBlock = allParkingSlots.stream()
                .filter(parkingSlot -> {
                    List<ParkingSlotReservation> reservations = parkingSlotReservationRepository.findByParkingSlot(parkingSlot);
                    return reservations.stream()
                            .noneMatch(reservation -> {
                                LocalDateTime reservationEnd = reservation.getStartTimestamp().toLocalDateTime().plusMinutes(reservation.getDurationInMinutes());
                                LocalDateTime desiredEnd = startLocalDateTime.plusMinutes(durationInMinutes);
                                return startLocalDateTime.isBefore(reservationEnd) && desiredEnd.isAfter(reservation.getStartTimestamp().toLocalDateTime());
                            });
                })
                .collect(Collectors.groupingBy(ParkingSlot::getBlock));

        List<AvailableParkingSlotsInfo> result = new ArrayList<>();
        for (Map.Entry<Block, List<ParkingSlot>> entry : availableSlotsByBlock.entrySet()) {
            AvailableParkingSlotsInfo info = new AvailableParkingSlotsInfo();
            info.setParkingLot(entry.getKey().getParkingLot());
            info.setBlock(entry.getKey());
            info.setAvailableParkingSlots(entry.getValue());
            result.add(info);
        }

        return result;
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
