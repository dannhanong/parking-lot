package com.hotrodoan.controller;

import com.hotrodoan.model.Block;
import com.hotrodoan.model.ParkingLot;
import com.hotrodoan.model.dto.ResponseMessage;
import com.hotrodoan.service.BlockService;
import com.hotrodoan.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blocks")
@CrossOrigin(origins = "*")
public class BlockController {
    @Autowired
    private BlockService blockService;
    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping("admin/all")
    public ResponseEntity<List<Block>> getBlocksByParkingLot() {
        return new ResponseEntity(blockService.getAllBlock(), HttpStatus.OK);
    }

    @PostMapping("admin/add")
    public ResponseEntity<Block> createBlock(@RequestBody Block block) {
        return new ResponseEntity(blockService.createBlock(block), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteBlock(@PathVariable Long id) {
        blockService.deleteBlock(id);
        return new ResponseEntity(new ResponseMessage("Deleted Block Success"), HttpStatus.OK);
    }

    @PutMapping("admin/update/{id}")
    public ResponseEntity<Block> updateBlock(@RequestBody Block block, @PathVariable Long id) {
        return new ResponseEntity(blockService.updateBlock(block, id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Block>> getBlocksByParkingLot(@RequestParam Long parkingLotId) {
        ParkingLot parkingLot = parkingLotService.getParkingLot(parkingLotId);
        return new ResponseEntity(blockService.getBlockByParkingLot(parkingLot), HttpStatus.OK);
    }
}
