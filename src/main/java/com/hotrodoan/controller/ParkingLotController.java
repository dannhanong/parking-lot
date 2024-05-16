package com.hotrodoan.controller;

import com.hotrodoan.model.ParkingLot;
import com.hotrodoan.model.ParkingLotDetails;
import com.hotrodoan.model.dto.ResponseMessage;
import com.hotrodoan.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-lots")
@CrossOrigin(origins = "*")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping("")
    public ResponseEntity<Page<ParkingLot>> getAllParkingLots(@RequestParam(defaultValue = "") String keyword,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(parkingLotService.getAllParkingLots(keyword, pageable), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ParkingLot> createParkingLot(@RequestBody ParkingLot parkingLot) {
        return new ResponseEntity<>(parkingLotService.createParkingLot(parkingLot), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingLot> updateParkingLot(@RequestBody ParkingLot parkingLot, @PathVariable Long id) {
        return new ResponseEntity<>(parkingLotService.updateParkingLot(parkingLot, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteParkingLot(@PathVariable Long id) {
        parkingLotService.deleteParkingLot(id);
        return new ResponseEntity<>(new ResponseMessage("Deleted parking lot successfully"), HttpStatus.OK);
    }

    @GetMapping("/mana")
    public ResponseEntity<?> countUsedParkingSlots() {
        return new ResponseEntity<>(parkingLotService.countUsedParkingSlots(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLotDetails> showParkingLot(@PathVariable Long id) {
        ParkingLot parkingLot = parkingLotService.showParkingLot(id);
        parkingLot.setUsedSlots(parkingLotService.countUsedParkingSlots(id));

        List<ParkingLot> suggestions = parkingLotService.getParkingLotsByAddressAndReentryAllowedAndIdNot(parkingLot.getAddress(), parkingLot.isReentryAllowed(), id);
        for (ParkingLot suggestion : suggestions) {
            suggestion.setUsedSlots(parkingLotService.countUsedParkingSlots(suggestion.getId()));
        }

        ParkingLotDetails parkingLotDetails = new ParkingLotDetails();
        parkingLotDetails.setParkingLot(parkingLot);
        parkingLotDetails.setSuggestions(suggestions);
        return new ResponseEntity<>(parkingLotDetails, HttpStatus.OK);
    }
}
