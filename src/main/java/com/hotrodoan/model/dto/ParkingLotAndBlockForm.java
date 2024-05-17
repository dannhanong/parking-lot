package com.hotrodoan.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotAndBlockForm {
    private String address;
    @Size(min = 5, max = 10)
    private String zip;
    private boolean reentryAllowed;
    private String operatingCompanyName;
    private boolean valetParkingAvailable;

    @NotBlank(message = "Block code is required")
    @Size(min = 1, max = 3)
    private String blockCode;
}
