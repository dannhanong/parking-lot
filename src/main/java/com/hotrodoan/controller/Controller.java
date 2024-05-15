package com.hotrodoan.controller;

import com.hotrodoan.model.VnPayPayment;
import com.hotrodoan.service.VNPayService;
import com.hotrodoan.service.VnPayPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
//@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private VnPayPaymentService vnPayPaymentService;

//    @Autowired
//    private Member_PackageService member_packageService;


    @GetMapping("/pay")
    public String home(){
        return "index";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

//        Object obj = request.getSession().getAttribute("m_p");
//        Member_Package m_p = (Member_Package) obj;
        HttpSession session = request.getSession();
//        Member_Package m_p = (Member_Package) session.getAttribute("m_p");

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = dateFormat.parse(paymentTime);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            VnPayPayment vnPayPayment = new VnPayPayment();
            vnPayPayment.setVnpAmount(totalPrice);
            vnPayPayment.setVnpOrderInfo(orderInfo);
            vnPayPayment.setVnpPayDate(sqlDate);
            vnPayPayment.setVnpTransactionNo(transactionId);
            vnPayPaymentService.addVnPayPayment(vnPayPayment);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (paymentStatus == 1){
//            member_packageService.addMember_Package(m_p);
            return "ordersuccess";
        }else
            return "orderfail";

//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}
