package com.hotrodoan.repository;

import com.hotrodoan.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long>{
    List<ParkingSlot> findByFloor(Floor floor);
//    @Query("SELECT p FROM ParkingSlot p " +
//            "WHERE p.slotAvailable = :slotAvailable " +
//            "AND p.floor = :floor")
//    List<ParkingSlot> findBySlotAvailableAndFloorBlockParkingLot(boolean slotAvailable,
//                                                                 Floor floor,
//                                                                 Block block,
//                                                                 ParkingLot parkingLot);
}
