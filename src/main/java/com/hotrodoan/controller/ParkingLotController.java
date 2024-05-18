package com.hotrodoan.controller;

import com.hotrodoan.model.Block;
import com.hotrodoan.model.ParkingLot;
import com.hotrodoan.model.ParkingLotDetails;
import com.hotrodoan.model.ParkingSlot;
import com.hotrodoan.model.dto.BlockAndParkingSlot;
import com.hotrodoan.model.dto.ParkingLotAndBlockForm;
import com.hotrodoan.model.dto.ResponseMessage;
import com.hotrodoan.service.BlockService;
import com.hotrodoan.service.ParkingLotService;
import com.hotrodoan.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-lots")
@CrossOrigin(origins = "*")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ParkingSlotService parkingSlotService;

    @GetMapping("")
    public ResponseEntity<Page<ParkingLot>> getAllParkingLots(@RequestParam(defaultValue = "") String keyword,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(parkingLotService.getAllParkingLots(keyword, pageable), HttpStatus.OK);
    }

    @PostMapping("/admin/add")
    public ResponseEntity<ParkingLotAndBlockForm> createParkingLot(@RequestBody ParkingLotAndBlockForm parkingLotAndBlockForm) {
        ParkingLot parkingLot = new ParkingLot();
        Block block = new Block();
        parkingLot.setSlotAvailable(true);
        parkingLot.setName(parkingLotAndBlockForm.getName());
        parkingLot.setAddress(parkingLotAndBlockForm.getAddress());
        parkingLot.setReentryAllowed(parkingLotAndBlockForm.isReentryAllowed());
        parkingLot.setOperatingCompanyName(parkingLotAndBlockForm.getOperatingCompanyName());
        parkingLot.setValetParkingAvailable(parkingLotAndBlockForm.isValetParkingAvailable());

        ParkingLot newParkingLot = parkingLotService.createParkingLot(parkingLot);
        int dBlock = 0;

        for (BlockAndParkingSlot blockAndParkingSlot : parkingLotAndBlockForm.getBlockAndParkingSlots()) {
            Block bl = new Block();
            bl.setBlockCode(blockAndParkingSlot.getBlockCode());
            bl.setParkingLot(newParkingLot);
            dBlock += 1;
            int dParkingSlot = 0;
            Block newBlock = blockService.createBlock(bl);

            int numberOfSlots = blockAndParkingSlot.getNumberOfParkingSlots();
            for (int i = 0; i < numberOfSlots; i++) {
                dParkingSlot += 1;
                ParkingSlot parkingSlot = new ParkingSlot();
                parkingSlot.setBlock(newBlock);
                parkingSlot.setSlotNumber(i + 1);
                parkingSlotService.addParkingSlot(parkingSlot);
            }
            newBlock.setNumberOfParkingSlots(dParkingSlot);
            blockService.updateBlock(newBlock, newBlock.getId());
        }

        newParkingLot.setNumberOfBlocks(dBlock);
        parkingLotService.updateParkingLot(newParkingLot, newParkingLot.getId());

        return new ResponseEntity<>(parkingLotAndBlockForm, HttpStatus.OK);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ParkingLotAndBlockForm> updateParkingLot(@RequestBody ParkingLotAndBlockForm parkingLotAndBlockForm, @PathVariable Long id) {
        ParkingLot parkingLot = new ParkingLot();
        Block block = new Block();
        parkingLot.setSlotAvailable(true);
        parkingLot.setName(parkingLotAndBlockForm.getName());
        parkingLot.setAddress(parkingLotAndBlockForm.getAddress());
        parkingLot.setReentryAllowed(parkingLotAndBlockForm.isReentryAllowed());
        parkingLot.setOperatingCompanyName(parkingLotAndBlockForm.getOperatingCompanyName());
        parkingLot.setValetParkingAvailable(parkingLotAndBlockForm.isValetParkingAvailable());

        ParkingLot updateParkingLot = parkingLotService.updateParkingLot(parkingLot, id);

        int dBlock = 0;

        for (BlockAndParkingSlot blockAndParkingSlot : parkingLotAndBlockForm.getBlockAndParkingSlots()) {
            Block bl = blockService.getBlockByParkingLotAndBlockCode(updateParkingLot, blockAndParkingSlot.getBlockCode());
            bl.setBlockCode(blockAndParkingSlot.getBlockCode());
            dBlock += 1;
            int dParkingSlot = 0;
            Block newBlock = blockService.createBlock(bl);

            int numberOfSlots = blockAndParkingSlot.getNumberOfParkingSlots();
            for (int i = 0; i < numberOfSlots; i++) {
                dParkingSlot += 1;
                ParkingSlot parkingSlot = new ParkingSlot();
                parkingSlot.setBlock(newBlock);
                parkingSlot.setSlotNumber(i + 1);
                parkingSlotService.addParkingSlot(parkingSlot);
            }
            newBlock.setNumberOfParkingSlots(dParkingSlot);
            blockService.updateBlock(newBlock, newBlock.getId());
        }

//        newParkingLot.setNumberOfBlocks(dBlock);
//        parkingLotService.updateParkingLot(newParkingLot, newParkingLot.getId());

        return new ResponseEntity<>(parkingLotAndBlockForm, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteParkingLot(@PathVariable Long id) {
        parkingLotService.deleteParkingLot(id);
        return new ResponseEntity<>(new ResponseMessage("Deleted parking lot successfully"), HttpStatus.OK);
    }

//    @GetMapping("/mana")
//    public ResponseEntity<?> countUsedParkingSlots() {
//        return new ResponseEntity<>(parkingLotService.countUsedParkingSlots(), HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLotDetails> showParkingLot(@PathVariable Long id) {
        ParkingLot parkingLot = parkingLotService.showParkingLot(id);
        parkingLot.setUsedSlots(parkingLotService.countUsedParkingSlots(id));

        List<ParkingLot> suggestions = parkingLotService.getParkingLotsByAddressAndReentryAllowedAndIdNot(parkingLot.getAddress(), parkingLot.isReentryAllowed(), id);
        for (ParkingLot suggestion : suggestions) {
            suggestion.setUsedSlots(parkingLotService.countUsedParkingSlots(suggestion.getId()));
        }

        ParkingLotDetails parkingLotDetails = new ParkingLotDetails();
        parkingLotDetails.setParkingLot(parkingLot);
        parkingLotDetails.setSuggestions(suggestions);
        return new ResponseEntity<>(parkingLotDetails, HttpStatus.OK);
    }
//
//    @PutMapping("admin/update/{id}")
//    public ResponseEntity<ParkingLot> updateParkingLotAdmin(@RequestBody ParkingLot parkingLot, @PathVariable Long id) {
//        return new ResponseEntity<>(parkingLotService.updateParkingLot(parkingLot, id), HttpStatus.OK);
//    }
}
