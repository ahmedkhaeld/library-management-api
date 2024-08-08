package com.library.borrowing.controller;

import com.library.borrowing.dto.BorrowingRecordDTO;
import com.library.borrowing.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @Autowired
    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDTO> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecordDTO borrowingRecord = borrowingRecordService.borrowBook(bookId, patronId);
        return new ResponseEntity<>(borrowingRecord, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDTO> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecordDTO borrowingRecord = borrowingRecordService.returnBook(bookId, patronId);
        return ResponseEntity.ok(borrowingRecord);
    }
}