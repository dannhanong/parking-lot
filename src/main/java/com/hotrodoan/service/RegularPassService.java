package com.hotrodoan.service;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.RegularPass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegularPassService {
    RegularPass addRegularPass(RegularPass regularPass);
    Page<RegularPass> getAllRegularPass(Pageable pageable);
    RegularPass getRegularPass(Long id);
    void deleteRegularPass(Long id);
    RegularPass updateRegularPass(RegularPass regularPass, Long id);
    RegularPass getRegularByCustomer(Customer customer);
    Page<RegularPass> getByCustomerName(String name, Pageable pageable);
}
