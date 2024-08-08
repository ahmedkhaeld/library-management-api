package com.library.borrowing.service;

import com.library.book.entity.Book;
import com.library.book.enums.BookStatus;
import com.library.book.repository.BookRepository;
import com.library.borrowing.dto.BorrowingRecordDTO;
import com.library.borrowing.entity.BorrowingRecord;
import com.library.borrowing.repository.BorrowingRecordRepository;
import com.library.patron.entity.Patron;
import com.library.patron.repository.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository,
                                  BookRepository bookRepository,
                                  PatronRepository patronRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Transactional
    public BorrowingRecordDTO borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new EntityNotFoundException("Patron not found with id: " + patronId));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for borrowing" + bookId);
        }

        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowDate.plusDays(14); // Assuming a 2-week borrowing period

        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, borrowDate, dueDate);
        book.setStatus(BookStatus.BORROWED);

        BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);
        bookRepository.save(book);

        return convertToDTO(savedRecord);
    }

    @Transactional
    public BorrowingRecordDTO returnBook(Long bookId, Long patronId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository
                .findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new EntityNotFoundException("No active borrowing record found for this book and patron" +patronId));

        Book book = borrowingRecord.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        borrowingRecord.setReturnDate(LocalDateTime.now());

        BorrowingRecord updatedRecord = borrowingRecordRepository.save(borrowingRecord);
        bookRepository.save(book);

        return convertToDTO(updatedRecord);
    }

    private BorrowingRecordDTO convertToDTO(BorrowingRecord record) {
        BorrowingRecordDTO dto = new BorrowingRecordDTO();
        dto.setId(record.getId());
        dto.setBookId(record.getBook().getId());
        dto.setPatronId(record.getPatron().getId());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setReturnDate(record.getReturnDate());
        return dto;
    }
}