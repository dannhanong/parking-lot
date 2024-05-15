package com.hotrodoan.service.impl;

import com.hotrodoan.model.Customer;
import com.hotrodoan.model.RegularPass;
import com.hotrodoan.repository.RegularPassRepository;
import com.hotrodoan.service.RegularPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class RegularPassServiceImpl implements RegularPassService {
    @Autowired
    private RegularPassRepository regularPassRepository;
    @Override
    public RegularPass addRegularPass(RegularPass regularPass) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(regularPass.getStartDate());
        calendar.add(Calendar.DATE, regularPass.getDurationInDays());
        regularPass.setEndDate(new Date(calendar.getTimeInMillis()));
        regularPass.setCost(regularPass.getDurationInDays() * 5000);
        regularPass.setPurchaseDate(new Date(System.currentTimeMillis()));
        return regularPassRepository.save(regularPass);
    }

    @Override
    public Page<RegularPass> getAllRegularPass(Pageable pageable) {
        return regularPassRepository.findAll(pageable);
    }

    @Override
    public RegularPass getRegularPass(Long id) {
        return regularPassRepository.findById(id).orElseThrow(() -> new RuntimeException("Regular pass not found"));
    }

    @Override
    public void deleteRegularPass(Long id) {
        regularPassRepository.deleteById(id);
    }

    @Override
    public RegularPass updateRegularPass(RegularPass regularPass, Long id) {
        return regularPassRepository.findById(id).map(r -> {
            r.setCustomer(regularPass.getCustomer());
            r.setPurchaseDate(regularPass.getPurchaseDate());
            r.setStartDate(regularPass.getStartDate());
            r.setEndDate(regularPass.getEndDate());
            r.setDurationInDays(regularPass.getDurationInDays());
            r.setCost(regularPass.getCost());
            return regularPassRepository.save(r);
        }).orElseThrow(() -> new RuntimeException("Regular pass not found"));
    }

    @Override
    public RegularPass getRegularByCustomer(Customer customer) {
        return regularPassRepository.findByCustomer(customer);
    }
}
