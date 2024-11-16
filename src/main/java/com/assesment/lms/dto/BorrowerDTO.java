package com.assesment.lms.dto;


import lombok.Builder;

/*
 * Data Transfer Object for Borrower
 */
@Builder
public record BorrowerDTO (
    Long id,
    String email,
    String name){}