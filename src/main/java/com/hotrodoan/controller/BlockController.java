package com.hotrodoan.controller;

import com.hotrodoan.model.Block;
import com.hotrodoan.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blocks")
public class BlockController {
    @Autowired
    private BlockService blockService;

    @GetMapping("")
    public ResponseEntity<List<Block>> getBlocksByParkingLot() {
        return new ResponseEntity(blockService.getAllBlock(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Block> createBlock(@RequestBody Block block) {
        return new ResponseEntity(blockService.createBlock(block), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBlock(@PathVariable Long id) {
        blockService.deleteBlock(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
