package com.hotrodoan.controller;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.ParkingSlotReservation;
import com.hotrodoan.model.User;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.CustomerService;
import com.hotrodoan.service.ParkingSlotReservationService;
import com.hotrodoan.service.UserService;
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

    @PostMapping("/add")
    public ResponseEntity<ParkingSlotReservation> createParkingSlotReservation(@RequestBody ParkingSlotReservation parkingSlotReservation) {
        return new ResponseEntity<>(parkingSlotReservationService.createParkingSlotReservation(parkingSlotReservation), HttpStatus.OK);
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
