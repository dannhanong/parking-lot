package com.hotrodoan.repository;

import com.hotrodoan.model.Block;
import com.hotrodoan.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long>{
//    @Query("SELECT p FROM ParkingSlot p " +
//            "WHERE p.slotAvailable = :slotAvailable " +
//            "AND p.floor = :floor")
//    List<ParkingSlot> findBySlotAvailableAndFloorBlockParkingLot(boolean slotAvailable,
//                                                                 Floor floor,
//                                                                 Block block,
//                                                                 ParkingLot parkingLot);
    List<ParkingSlot> findByBlock(Block block);
    @Query("SELECT COUNT(ps) FROM ParkingSlot ps " +
            "WHERE ps.block.id = :id AND ps.slotAvailable = false")
    int countUsedParkingSlots(Long id);
}
