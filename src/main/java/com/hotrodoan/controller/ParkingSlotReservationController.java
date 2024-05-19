package com.hotrodoan.controller;

import com.hotrodoan.model.*;
import com.hotrodoan.model.dto.AvailableParkingSlotsInfo;
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
@CrossOrigin(origins = "*")
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
    private ParkingSlotService parkingSlotService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private RegularPassService regularPassService;

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
    public ResponseEntity<List<AvailableParkingSlotsInfo>> getAllParkingSlotAvailable(@RequestParam("startTimestamp") String startTimestampStr,
                                                                                      @RequestParam("durationInMinutes") int durationInMinutes,
                                                                                      @RequestParam("id") Long id) {
        Timestamp startTimestamp = Timestamp.valueOf(startTimestampStr);
        return new ResponseEntity<>(parkingSlotReservationService.findAvailableParkingSlotsAndBlockAndParkingLot(startTimestamp, durationInMinutes, id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ParkingSlotReservation> createParkingSlotReservation(HttpServletRequest request, @RequestBody ParkingSlotReservation parkingSlotReservation) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerService.getCustomerByUser(user);
        parkingSlotReservation.setCustomer(customer);
        int cost = 500 * parkingSlotReservation.getDurationInMinutes();
        RegularPass regularPass = regularPassService.getRegularByCustomer(customer);
        if (regularPass != null) {
            parkingSlotReservation.setCost(0);
        } else {
            parkingSlotReservation.setCost(cost);
        }
        ParkingSlotReservation newParkingSlotReservation = parkingSlotReservationService.createParkingSlotReservation(parkingSlotReservation);

        ParkingSlot parkingSlot = parkingSlotReservation.getParkingSlot();

        if(parkingSlot.isSlotAvailable() == false) {
            throw new RuntimeException("Parking slot is not available");
        }else{
            Long parkingSlotId = parkingSlot.getId();
            parkingSlot = parkingSlotService.getParkingSlot(parkingSlotId);
            parkingSlot.setSlotAvailable(false);
            parkingSlotService.updateParkingSlot(parkingSlot, parkingSlotId);
            return new ResponseEntity<>(newParkingSlotReservation, HttpStatus.OK);
        }     
    }

    public boolean isParkingSlotAvailable(ParkingSlot parkingSlot) {
        return parkingSlot.isSlotAvailable();
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
