package com.hotrodoan.controller;

import com.hotrodoan.model.ParkingSlip;
import com.hotrodoan.model.dto.ResponseMessage;
import com.hotrodoan.service.ParkingSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-slips")
public class ParkingSlipController {
    @Autowired
    private ParkingSlipService parkingSlipService;

    @GetMapping("")
    public ResponseEntity<Page<ParkingSlip>> getAllParkingSlips(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "id") String sortBy,
                                                               @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new  ResponseEntity(parkingSlipService.getAllParkingSlip(pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteParkingSlip(@PathVariable Long id) {
        parkingSlipService.deleteParkingSlip(id);
        return new ResponseEntity<>(new ResponseMessage("Deleted parking slip successfully"), HttpStatus.OK);
    }
}
