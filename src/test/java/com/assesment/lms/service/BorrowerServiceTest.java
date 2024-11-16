package com.assesment.lms.service;

import com.assesment.lms.dto.BorrowerDTO;
import com.assesment.lms.entity.Book;
import com.assesment.lms.entity.Borrower;
import com.assesment.lms.exception.ResourceNotFoundException;
import com.assesment.lms.repository.BookRepository;
import com.assesment.lms.repository.BorrowerRepository;
import com.assesment.lms.service.impl.BorrowerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @Test
    void testRegisterBorrower() {
        // Given
        BorrowerDTO borrowerDTO = new BorrowerDTO(null, "test@example.com", "Test Borrower");
        Borrower savedBorrower = new Borrower(1L, "test@example.com", "Test Borrower");

        when(modelMapper.map(borrowerDTO, Borrower.class)).thenReturn(new Borrower());
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedBorrower);
        when(modelMapper.map(savedBorrower, BorrowerDTO.class)).thenReturn(borrowerDTO);

        // When
        BorrowerDTO result = borrowerService.registerBorrower(borrowerDTO);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.email());
        assertEquals("Test Borrower", result.name());
    }

    @Test
    void testBorrowBook() {
        // Given
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        assertDoesNotThrow(() -> borrowerService.borrowBook(1L, 1L));

        // Then
        assertNotNull(book.getBorrower());
        assertEquals(borrower, book.getBorrower());
    }

    @Test
    void testBorrowBookAlreadyBorrowed() {
        // Given
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        book.setBorrower(borrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When, Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.borrowBook(1L, 1L));
        assertEquals("Book borrowed by another borrower.", exception.getMessage());
    }

    @Test
    void testReturnBook() {
        // Given
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        book.setBorrower(borrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        assertDoesNotThrow(() -> borrowerService.returnBook(1L, 1L));

        // Then
        assertNull(book.getBorrower());
    }

    @Test
    void testReturnBookNotBorrowed() {
        // Given
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Borrower anotherBorrower = new Borrower(2L, "another@example.com", "Another Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        book.setBorrower(anotherBorrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When, Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.returnBook(1L, 1L));
        assertEquals("This Book was not issued", exception.getMessage());
    }

    @Test
    void testFetchAllBorrowers() {
        // Given
        Borrower borrower1 = new Borrower(1L, "borrower1@example.com", "Borrower 1");
        Borrower borrower2 = new Borrower(2L, "borrower2@example.com", "Borrower 2");
        List<Borrower> borrowers = Arrays.asList(borrower1, borrower2);

        when(borrowerRepository.findAll()).thenReturn(borrowers);
        when(modelMapper.map(borrower1, BorrowerDTO.class)).thenReturn(new BorrowerDTO(1L, "borrower1@example.com", "Borrower 1"));
        when(modelMapper.map(borrower2, BorrowerDTO.class)).thenReturn(new BorrowerDTO(2L, "borrower2@example.com", "Borrower 2"));

        // When
        List<BorrowerDTO> result = borrowerService.fetchAllBorrowers();

        // Then
        assertEquals(2, result.size());
        assertEquals("borrower1@example.com", result.get(0).email());
        assertEquals("Borrower 2", result.get(1).name());
    }

    @Test
    void testBorrowBook_BorrowerNotFound() {
        // Given
        Long borrowerId = 1L;
        Long bookId = 1L;

        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.borrowBook(borrowerId, bookId));

        // Then
        assertEquals("Borrower associated with id: " + borrowerId + " doesn't exist", exception.getMessage());
        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        // Given
        Long borrowerId = 1L;
        Long bookId = 1L;
        Borrower borrower = new Borrower(borrowerId, "borrower@example.com", "Test Borrower");

        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.borrowBook(borrowerId, bookId));

        // Then
        assertEquals("Book associated with id: " + bookId+ " doesn't exist", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

}
