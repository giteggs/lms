// BorrowerServiceImpl.java
package com.assesment.lms.service.impl;

import com.assesment.lms.dto.BorrowerDTO;
import com.assesment.lms.exception.BookBorrowedException;
import com.assesment.lms.exception.BookBorrowerConflictException;
import com.assesment.lms.exception.BorrowerEmailAlreadyExistsException;
import com.assesment.lms.mapper.BorrowerMapper;
import com.assesment.lms.repository.BookRepository;
import com.assesment.lms.repository.BorrowerRepository;
import com.assesment.lms.entity.Book;
import com.assesment.lms.entity.Borrower;
import com.assesment.lms.exception.ResourceNotFoundException;
import com.assesment.lms.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final BorrowerMapper borrowerMapper;

    @Override
    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        if (borrowerRepository.existsByEmail(borrowerDTO.email())) {
            throw new BorrowerEmailAlreadyExistsException("A borrower account with this email address already exists.");
        }

        Borrower borrower = borrowerMapper.toBorrower(borrowerDTO);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return borrowerMapper.toBorrowerDTO(savedBorrower);
    }
    @Override
    public void borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower associated with id: " + borrowerId + " doesn't exist"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book associated with id: " + bookId + " doesn't exist"));

        if (book.getBorrower() != null) {
            throw new BookBorrowedException("Book borrowed by another borrower.");
        }

        book.setBorrower(borrower);

        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower associated with id: " + borrowerId + " doesn't exist"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book associated with id: " + bookId + " doesn't exist"));

        if ( book.getBorrower() ==null || !book.getBorrower().equals(borrower)) {
            throw new BookBorrowerConflictException("This Book was not issued");
        }

        book.setBorrower(null);

        bookRepository.save(book);
    }

    @Override
    public List<BorrowerDTO> fetchAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(borrowerMapper::toBorrowerDTO)
                .toList();
    }

    @Override
    public BorrowerDTO fetchBorrowerById(Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower associated with id: " + borrowerId + " doesn't exist"));
        return borrowerMapper.toBorrowerDTO(borrower);
    }

}