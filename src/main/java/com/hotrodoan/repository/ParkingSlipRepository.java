package com.hotrodoan.repository;

import com.hotrodoan.model.ParkingSlip;
import com.hotrodoan.model.ParkingSlotReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlipRepository extends JpaRepository<ParkingSlip, Long>{
    ParkingSlip findByParkingSlotReservation(ParkingSlotReservation parkingSlotReservation);
}
