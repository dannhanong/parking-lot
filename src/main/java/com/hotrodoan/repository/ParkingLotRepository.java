package com.hotrodoan.repository;

import com.hotrodoan.model.ParkingLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    @Query("SELECT p FROM ParkingLot p WHERE CONCAT(p.address, ' ', p.zip, ' ', p.operatingCompanyName) LIKE %:keyword%")
    Page<ParkingLot> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT pl.address, COUNT(ps) FROM ParkingSlot ps " +
            "JOIN ps.floor f " +
            "JOIN f.block b " +
            "JOIN b.parkingLot pl " +
            "WHERE ps.slotAvailable = false " +
            "GROUP BY pl.address")
    List<Object[]> countUsedParkingSlotsPerParkingLot();
}
