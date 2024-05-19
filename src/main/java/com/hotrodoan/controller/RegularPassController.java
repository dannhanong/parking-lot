package com.hotrodoan.controller;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.RegularPass;
import com.hotrodoan.model.User;
import com.hotrodoan.model.dto.ResponseMessage;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.CustomerService;
import com.hotrodoan.service.RegularPassService;
import com.hotrodoan.service.UserService;
import com.hotrodoan.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Date;
import java.util.Calendar;

@RestController
@RequestMapping("/regular-passes")
@CrossOrigin(origins = "*")
public class RegularPassController {
    @Autowired
    private RegularPassService regularPassService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/admin")
    public ResponseEntity<Page<RegularPass>> getAllRegularPass(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "id") String sortBy,
                                                               @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        Page<RegularPass> regularPasses = regularPassService.getAllRegularPass(pageable);
        return new ResponseEntity<>(regularPasses, regularPasses.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<RegularPass> showRegularPass(HttpServletRequest request,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "desc") String order) {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerService.getCustomerByUser(user);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        RegularPass regularPass = regularPassService.getRegularByCustomer(customer);
        return new ResponseEntity<>(regularPass, HttpStatus.OK);
    }

    @PostMapping("/add")
    public RedirectView addRegularPass(HttpServletRequest request, HttpSession session, @RequestBody RegularPass regularPass) {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerService.getCustomerByUser(user);
        regularPass.setCustomer(customer);
        int totalPrice = 5000*regularPass.getDurationInDays();
        int duration = regularPass.getDurationInDays();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(totalPrice, "Pay for "+duration+" day", baseUrl);
        session.setAttribute("regularPass", regularPass);
        // regularPassService.addRegularPass(regularPass);
//        return new ResponseEntity<>(regularPassService.addRegularPass(regularPass), HttpStatus.CREATED);
        return new RedirectView(vnpayUrl);
    }

    @PutMapping("renew")
    public String renewRegularPass(HttpServletRequest request, @RequestBody RegularPass regularPass) {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerService.getCustomerByUser(user);
        RegularPass r = regularPassService.getRegularByCustomer(customer);

//        String vnpayUrl = "";
        if (r.getEndDate().after(regularPass.getStartDate())) {
            int totalPrice = 5000 * regularPass.getDurationInDays();
            int duration = regularPass.getDurationInDays();
            Date oldEndDate = r.getEndDate();
            Calendar c = Calendar.getInstance();
            c.setTime(oldEndDate);
            c.add(Calendar.DATE, regularPass.getDurationInDays());
            Date newEndDate = new Date(c.getTimeInMillis());
            regularPass.setEndDate(newEndDate);

            regularPass.setDurationInDays(r.getDurationInDays() + regularPass.getDurationInDays());
            regularPass.setStartDate(r.getStartDate());
            regularPass.setCustomer(customer);
            regularPass.setPurchaseDate(new Date(System.currentTimeMillis()));
            regularPass.setCost(5000 * regularPass.getDurationInDays());

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(totalPrice, "Pay for " + duration + " day", baseUrl);
            //        return new ResponseEntity<>(regularPassService.updateRegularPass(regularPass, r.getId()), HttpStatus.OK);
            return vnpayUrl;
        }
        else {
            return new ResponseMessage("Cannot renew before the end date of the current pass").getMessage();
        }

    }
}
