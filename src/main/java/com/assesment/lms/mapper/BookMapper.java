package com.assesment.lms.mapper;

import com.assesment.lms.dto.BookDTO;
import com.assesment.lms.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDTO toBookDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getBorrower() != null ?book.getBorrower().getId(): null);
    }

    public Book toBook(BookDTO bookDTO) {
        return new Book(bookDTO.id(), bookDTO.title(), bookDTO.author(), bookDTO.isbn(), null);
    }
}
