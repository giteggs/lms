package com.assesment.lms.dto;

import lombok.Builder;

/*
 * Data Transfer Object for Book
 */
@Builder
public record BookDTO(
    Long id,
    String isbn,
    String title,
    String author,
    Long borrowerId){

}
