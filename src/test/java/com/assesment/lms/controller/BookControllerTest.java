package com.assesment.lms.controller;

import com.assesment.lms.dto.BookDTO;
import com.assesment.lms.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO mockBookDTO;

    @BeforeEach
    void setUp() {
        mockBookDTO = new BookDTO(1L, "978-0134670958", "Effective Java", "Joshua Bloch", null);
    }

    @Test
    void testRegisterBook() {
        when(bookService.registerBook(any(BookDTO.class))).thenReturn(mockBookDTO);

        ResponseEntity<BookDTO> responseEntity = bookController.registerBook(mockBookDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockBookDTO, responseEntity.getBody());
    }

    @Test
    void testGetAllBooks() {
        List<BookDTO> mockBooks = Collections.singletonList(mockBookDTO);
        when(bookService.fetchAllBooks()).thenReturn(mockBooks);

        ResponseEntity<List<BookDTO>> responseEntity = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks, responseEntity.getBody());
    }
}
