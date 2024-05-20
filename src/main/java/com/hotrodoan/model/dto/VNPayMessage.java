package com.hotrodoan.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VNPayMessage {
    private String message;
    private String vnpayUrl;
}
