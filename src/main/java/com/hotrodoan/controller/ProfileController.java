package com.hotrodoan.controller;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.User;
import com.hotrodoan.model.dto.ParkingLotAndBlockForm;
import com.hotrodoan.model.dto.UpdateProfileForm;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.CustomerService;
import com.hotrodoan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @GetMapping("")
    public ResponseEntity<UpdateProfileForm> getCustomer(HttpServletRequest request) {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        UpdateProfileForm updateProfileForm = new UpdateProfileForm();
        Customer customer = customerService.getCustomerByUser(user);
        updateProfileForm.setName(user.getName());
        updateProfileForm.setUsername(user.getUsername());
        updateProfileForm.setEmail(user.getEmail());
        updateProfileForm.setAvatar(user.getAvatar());
        updateProfileForm.setContactNumber(customer.getContactNumber());
        updateProfileForm.setVehicleNumber(customer.getVehicleNumber());
        updateProfileForm.setRegularCustomer(customer.isRegularCustomer());
        return new ResponseEntity(updateProfileForm, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateProfileForm> updateCustomer(HttpServletRequest request, @RequestBody UpdateProfileForm updateProfileForm) {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer1 = customerService.getCustomerByUser(user);

        user.setName(updateProfileForm.getName());
        user.setEmail(updateProfileForm.getEmail());
        user.setAvatar(updateProfileForm.getAvatar());

        customer1.setVehicleNumber(updateProfileForm.getVehicleNumber());
        customer1.setContactNumber(updateProfileForm.getContactNumber());

        userService.save(user);
        customerService.updateCustomer(customer1, customer1.getId());
        return new ResponseEntity<>(updateProfileForm, HttpStatus.OK);
    }
}
