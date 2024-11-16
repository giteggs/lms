package com.assesment.lms.mapper;

import com.assesment.lms.dto.BorrowerDTO;
import com.assesment.lms.entity.Borrower;
import org.springframework.stereotype.Component;

@Component
public class BorrowerMapper {
    public BorrowerDTO toBorrowerDTO(Borrower borrower) {
        return BorrowerDTO.builder()
                .id(borrower.getId())
                .email(borrower.getEmail())
                .name(borrower.getName())
                .build();
    }

    public Borrower toBorrower(BorrowerDTO borrowerDTO) {
        return Borrower.builder()
                .id(borrowerDTO.id())
                .email(borrowerDTO.email())
                .name(borrowerDTO.name())
                .build();
    }
}
