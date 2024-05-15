package com.hotrodoan.controller;

import com.hotrodoan.model.*;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/parking-slot-reservations")
public class ParkingSlotReservationController {
    @Autowired
    private ParkingSlotReservationService parkingSlotReservationService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ParkingSlipService parkingSlipService;
    @Autowired
    private ParkingSlotService parkingSlotService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping("/admin/all")
    public ResponseEntity<Page<ParkingSlotReservation>> getAllParkingSlotReservations(@RequestParam(defaultValue = "")Date date,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size,
                                                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                                                      @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(parkingSlotReservationService.getAllParkingSlotReservations(date, pageable), HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<Page<ParkingSlotReservation>> showParkingSlotReservations(HttpServletRequest request,
                                                                                    @RequestParam(defaultValue = "")Date date,
                                                                                    @RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "10") int size,
                                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                                    @RequestParam(defaultValue = "desc") String order) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerService.getCustomerByUser(user);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(parkingSlotReservationService.getAllParkingSlotReservationsByCustomer(customer, date, pageable), HttpStatus.OK);
    }

    @GetMapping("/add")
    public ResponseEntity<List<ParkingSlot>> getAllParkingSlotAvailable(@RequestParam("startTimestamp") String startTimestampStr,
                                                                        @RequestParam("durationInMinutes") int durationInMinutes,
                                                                        @RequestParam("blockId") Long blockId,
                                                                        @RequestParam("parkingLotId") Long parkingLotId) {
        Timestamp startTimestamp = Timestamp.valueOf(startTimestampStr);
        Block block = blockService.getBlock(blockId);
        ParkingLot parkingLot = parkingLotService.getParkingLot(parkingLotId);
        return new ResponseEntity<>(parkingSlotReservationService.findAvailableParkingSlots(startTimestamp, durationInMinutes, block, parkingLot), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ParkingSlotReservation> createParkingSlotReservation(HttpServletRequest request, @RequestBody ParkingSlotReservation parkingSlotReservation) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerService.getCustomerByUser(user);
        parkingSlotReservation.setCustomer(customer);
        ParkingSlotReservation newParkingSlotReservation = parkingSlotReservationService.createParkingSlotReservation(parkingSlotReservation);

        ParkingSlot parkingSlot = parkingSlotReservation.getParkingSlot();
        Long parkingSlotId = parkingSlot.getId();
        parkingSlot = parkingSlotService.getParkingSlot(parkingSlotId);
        parkingSlot.setSlotAvailable(false);
        parkingSlotService.updateParkingSlot(parkingSlot, parkingSlotId);

        ParkingSlip parkingSlip = new ParkingSlip();
        parkingSlip.setParkingSlotReservation(newParkingSlotReservation);
        parkingSlipService.createParkingSlip(parkingSlip);
        return new ResponseEntity<>(newParkingSlotReservation, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ParkingSlotReservation> updateParkingSlotReservation(@RequestBody ParkingSlotReservation parkingSlotReservation, @PathVariable Long id) {
        return new ResponseEntity<>(parkingSlotReservationService.updateParkingSlotReservation(parkingSlotReservation, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteParkingSlotReservation(@PathVariable Long id) {
        parkingSlotReservationService.deleteParkingSlotReservation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}