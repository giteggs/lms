// BookServiceImpl.java
package com.assesment.lms.service.impl;

import com.assesment.lms.dto.BookDTO;
import com.assesment.lms.mapper.BookMapper;
import com.assesment.lms.repository.BookRepository;
import com.assesment.lms.repository.BorrowerRepository;
import com.assesment.lms.entity.Book;
import com.assesment.lms.entity.Borrower;
import com.assesment.lms.exception.ResourceNotFoundException;
import com.assesment.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BorrowerRepository borrowerRepository;


    public BookDTO registerBook(BookDTO bookDTO) {
        validateBook(bookDTO);
        Book book = bookMapper.toBook(bookDTO);
        Borrower borrower = getBorrowerIfExists(bookDTO);
        book.setBorrower(borrower);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(savedBook);
    }

    private void validateBook(BookDTO bookDTO) {
        List<Book> existingBooks = bookRepository.findByIsbn(bookDTO.isbn());
        for (Book existingBook : existingBooks) {
            if (!existingBook.getTitle().equals(bookDTO.title()) ||
                    !existingBook.getAuthor().equals(bookDTO.author())) {
                throw new IllegalArgumentException("Books with the same ISBN must have the same title and author");
            }
        }
    }

    private Borrower getBorrowerIfExists(BookDTO bookDTO) {
        return (bookDTO.id() != null && bookDTO.borrowerId() != 0)
                ? borrowerRepository.findById(bookDTO.borrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"))
                : null;
    }

    @Override
    public List<BookDTO> fetchAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDTO)
                .toList();
    }

    @Override
    public BookDTO fetchBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
    }
}