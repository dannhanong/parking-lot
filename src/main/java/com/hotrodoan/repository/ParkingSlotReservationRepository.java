package com.hotrodoan.repository;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.ParkingSlot;
import com.hotrodoan.model.ParkingSlotReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ParkingSlotReservationRepository extends JpaRepository<ParkingSlotReservation, Long> {
    Page<ParkingSlotReservation> findByBookingDate(Date date, Pageable pageable);
    Page<ParkingSlotReservation> findByCustomerAndBookingDate(Customer customer, Date date, Pageable pageable);
//    @Query("SELECT COUNT(p) > 0 FROM ParkingSlotReservation p WHERE p.slotNumber = :slotNumber " +
//            "AND :bookingDate BETWEEN p.bookingDate " +
//            "AND DATE_ADD(p.bookingDate, INTERVAL p.durationInMinutes MINUTE)")
//    boolean checkIfSlotIsAvailable(@Param("bookingDate") Date bookingDate, @Param("slotNumber") int slotNumber);

//    @Query("SELECT p FROM ParkingSlot p " +
//            "JOIN p.floor f " +
//            "JOIN f.block b " +
//            "JOIN b.parkingLot pl " +
//            "WHERE p.slotNumber = :slotNumber " +
//            "AND pl.address LIKE %:address% " +
//            "AND NOT EXISTS (SELECT 1 FROM ParkingSlotReservation r " +
//
//            "WHERE (:bookingDate BETWEEN r.bookingDate " + // Giữ lại p.bookingDate ở đây
//            "AND (r.bookingDate + INTERVAL *:durationInMinutes MINUTE))) " +
//            "AND (:bookingDate BETWEEN r.bookingDate " +
//            "AND (r.bookingDate + INTERVAL *:durationInMinutes MINUTE))")
//    List<ParkingSlot> findBySlotNumberAndBookingDateBetweenAndAddress(@Param("slotNumber") int slotNumber,
//                                                                      @Param("bookingDate") Date bookingDate,
//                                                                      @Param("durationInMinutes") int durationInMinutes,
//                                                                      @Param("address") String address);
}
