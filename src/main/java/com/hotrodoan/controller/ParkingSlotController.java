package com.hotrodoan.controller;

import com.hotrodoan.model.Block;
import com.hotrodoan.model.ParkingSlot;
import com.hotrodoan.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-slots")
public class ParkingSlotController {
    @Autowired
    private ParkingSlotService parkingSlotService;

    @GetMapping("")
    public ResponseEntity<List<ParkingSlot>> getAllParkingSlots(Block block) {
        return ResponseEntity.ok(parkingSlotService.getParkingSlotByBlock(block));
    }

    @PostMapping("/add")
    public ResponseEntity<ParkingSlot> addParkingSlot(@RequestBody ParkingSlot parkingSlot) {
        return new  ResponseEntity<>(parkingSlotService.addParkingSlot(parkingSlot), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ParkingSlot> updateParkingSlot(@RequestBody ParkingSlot parkingSlot, @PathVariable Long id) {
        return ResponseEntity.ok(parkingSlotService.updateParkingSlot(parkingSlot, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteParkingSlot(@PathVariable Long id) {
        parkingSlotService.deleteParkingSlot(id);
        return ResponseEntity.ok().build();
    }
}
