// BorrowerController.java
package com.assesment.lms.controller;

import com.assesment.lms.dto.BorrowerDTO;
import com.assesment.lms.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.assesment.lms.constants.Constants.API_V1_LMS_BASE_PATH;

@RestController
@RequestMapping(API_V1_LMS_BASE_PATH + "/borrowers")
@Slf4j
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        log.info("Request to create borrower: {}", borrowerDTO);
        return new ResponseEntity<>(borrowerService.registerBorrower(borrowerDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        log.info("Request to borrow book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        log.info("Request to return book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        log.info("Request to get all borrowers");
        return ResponseEntity.ok(borrowerService.fetchAllBorrowers());
    }

    @GetMapping("/{borrowerId}")
    public ResponseEntity<BorrowerDTO> getBorrowerDetails(@PathVariable Long borrowerId) {
        log.info("Request to get borrower with id: {}", borrowerId);
        return ResponseEntity.ok(borrowerService.fetchBorrowerById(borrowerId));
    }
}