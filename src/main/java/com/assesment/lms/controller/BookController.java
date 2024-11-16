// BookController.java
package com.assesment.lms.controller;

import com.assesment.lms.dto.BookDTO;
import com.assesment.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.assesment.lms.constants.Constants.API_V1_LMS_BASE_PATH;

@RestController
@RequestMapping(API_V1_LMS_BASE_PATH)
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<BookDTO> registerBook(@RequestBody BookDTO bookDTO) {
        log.info("Request to create book: {}", bookDTO);
        try {
            BookDTO savedBook = bookService.registerBook(bookDTO);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.info("Request to get All books");
        return ResponseEntity.ok(bookService.fetchAllBooks());
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDTO> getBookDetails(@PathVariable Long bookId) {
        log.info("Request to get book with id: {}", bookId);
        return ResponseEntity.ok(bookService.fetchBookById(bookId));
    }
}
