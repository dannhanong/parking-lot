package com.hotrodoan.schedule;

import com.hotrodoan.model.BookingHistory;
import com.hotrodoan.model.ParkingSlip;
import com.hotrodoan.model.ParkingSlot;
import com.hotrodoan.model.ParkingSlotReservation;
import com.hotrodoan.service.BookingHistoryService;
import com.hotrodoan.service.ParkingSlipService;
import com.hotrodoan.service.ParkingSlotReservationService;
import com.hotrodoan.service.ParkingSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class BookingScheduler {
    private static final Logger logger = LoggerFactory.getLogger(BookingScheduler.class);
    @Autowired
    private ParkingSlotReservationService parkingSlotReservationService;
    @Autowired
    private BookingHistoryService bookingHistoryService;
    @Autowired
    private ParkingSlipService parkingSlipService;
    @Autowired
    private ParkingSlotService parkingSlotService;

    @Scheduled(fixedRate = 3600000)
    public void addHistoryFromBooking() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<ParkingSlotReservation> expiredBookings = parkingSlotReservationService.findPastReservations();
        for (ParkingSlotReservation booking : expiredBookings) {
            ParkingSlip parkingSlip = parkingSlipService.getParkingSlipByParkingSlotReservation(booking);

            Long parkingSlotId = booking.getParkingSlot().getId();
            ParkingSlot parkingSlot = parkingSlotService.getParkingSlot(parkingSlotId);
            parkingSlot.setSlotAvailable(true);
            parkingSlotService.updateParkingSlot(parkingSlot, parkingSlotId);

            BookingHistory history = new BookingHistory();
            history.setCustomer(booking.getCustomer());
            history.setStartTimestamp(booking.getStartTimestamp());
            history.setDurationInMinutes(booking.getDurationInMinutes());
            history.setBookingDate(booking.getBookingDate());
            history.setParkingSlot(booking.getParkingSlot());
            history.setActualEntryTime(parkingSlip.getActualEntryTime());
            history.setActualExitTime(parkingSlip.getActualExitTime());
            history.setBasicCost(parkingSlip.getBasicCost());
            history.setPenalty(parkingSlip.getPenalty());
            history.setTotalCost(parkingSlip.getTotalCost());
            history.setPaid(parkingSlip.isPaid());

            bookingHistoryService.addBookingHistory(history);
            parkingSlipService.deleteParkingSlip(parkingSlip.getId());
            parkingSlotReservationService.deleteParkingSlotReservation(booking.getId());
        }
    }
}