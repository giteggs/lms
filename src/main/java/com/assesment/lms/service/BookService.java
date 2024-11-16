// BookService.java
package com.assesment.lms.service;

import com.assesment.lms.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO registerBook(BookDTO bookDTO);
    List<BookDTO> fetchAllBooks();
    BookDTO fetchBookById(Long bookId);
}
