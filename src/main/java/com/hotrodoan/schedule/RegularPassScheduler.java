package com.hotrodoan.schedule;

import com.hotrodoan.model.RegularPass;
import com.hotrodoan.service.RegularPassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class RegularPassScheduler {
    private static final Logger logger = LoggerFactory.getLogger(BookingScheduler.class);
    @Autowired
    private RegularPassService regularPassService;

    @Scheduled(fixedRate = 600000)
    public void checkNewAndResetRenew() {
        List<RegularPass> regularPasses = regularPassService.getAllRegularPass();

        for (RegularPass regularPass : regularPasses) {
            if (regularPass.getStartDate().equals(new Date(System.currentTimeMillis())) || regularPass.getStartDate().before(new Date(System.currentTimeMillis()))){
                regularPass.setStatusNow(true);
                regularPassService.updateRegularPass(regularPass, regularPass.getId());
            }

            if (regularPassService.checkRenew(regularPass) == false) {
                regularPass.setRenewPair(false);
                regularPass.setStatusNow(false);
                regularPassService.updateRegularPass(regularPass, regularPass.getId());
            }
        }
    }
}
