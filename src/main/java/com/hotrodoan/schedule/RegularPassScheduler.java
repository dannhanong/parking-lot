package com.hotrodoan.schedule;

import com.hotrodoan.model.RegularPass;
import com.hotrodoan.service.RegularPassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegularPassScheduler {
    private static final Logger logger = LoggerFactory.getLogger(BookingScheduler.class);
    @Autowired
    private RegularPassService regularPassService;

    @Scheduled(fixedRate = 3600000)
    public void checkNewAndResetRenew() {
        List<RegularPass> regularPasses = regularPassService.getAllRegularPass();

        for (RegularPass regularPass : regularPasses) {
            if (regularPass.isPair() == false){
                regularPassService.deleteRegularPass(regularPass.getId());
            }else {
                if (regularPassService.checkRenew(regularPass) == false) {
                    regularPass.setRenewPair(false);
                    regularPassService.updateRegularPass(regularPass, regularPass.getId());
                }
            }
        }
    }
}
