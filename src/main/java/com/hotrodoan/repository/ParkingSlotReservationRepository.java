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
    List<ParkingSlotReservation> findByParkingSlot(ParkingSlot parkingSlot);
}
