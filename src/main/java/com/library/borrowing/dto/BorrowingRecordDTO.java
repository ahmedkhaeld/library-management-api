package com.library.borrowing.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BorrowingRecordDTO {

    private Long id;
    private Long bookId;
    private Long patronId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    public BorrowingRecordDTO() {}

    public BorrowingRecordDTO(Long bookId, Long patronId, LocalDateTime borrowDate, LocalDateTime dueDate) {
        this.bookId = bookId;
        this.patronId = patronId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "BorrowingRecordDTO{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", patronId=" + patronId +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}