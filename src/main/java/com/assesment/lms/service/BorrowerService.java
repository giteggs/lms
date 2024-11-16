
// BorrowerService.java
package com.assesment.lms.service;

import com.assesment.lms.dto.BorrowerDTO;

import java.util.List;

public interface BorrowerService {
    BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO);
    void borrowBook(Long borrowerId, Long bookId);
    void returnBook(Long borrowerId, Long bookId);
    List<BorrowerDTO> fetchAllBorrowers();
    BorrowerDTO fetchBorrowerById(Long borrowerId);
}