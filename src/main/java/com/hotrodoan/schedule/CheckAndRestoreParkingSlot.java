package com.hotrodoan.schedule;

import com.hotrodoan.service.BookingHistoryService;
import com.hotrodoan.service.ParkingSlotReservationService;
import com.hotrodoan.service.ParkingSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckAndRestoreParkingSlot {
    @Autowired
    private ParkingSlotReservationService parkingSlotReservationService;
    @Autowired
    private BookingHistoryService bookingHistoryService;
    @Autowired
    private ParkingSlotService parkingSlotService;
    private static final Logger logger = LoggerFactory.getLogger(BookingScheduler.class);

    @Scheduled(fixedRate = 3600000)
    public void restoreParkingSlot() {
        logger.info("Checking and restoring parking slot");
        parkingSlotReservationService.findPastReservations().forEach(booking -> {
            Long parkingSlotId = booking.getParkingSlot().getId();
            parkingSlotService.getParkingSlot(parkingSlotId).setSlotAvailable(true);
            parkingSlotService.updateParkingSlot(parkingSlotService.getParkingSlot(parkingSlotId), parkingSlotId);
            parkingSlotReservationService.deleteParkingSlotReservation(booking.getId());
        });
    }

}
