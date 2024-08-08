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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setStatus(BookStatus.AVAILABLE);

        patron = new Patron();
        patron.setId(1L);

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
    }

    @Test
    void borrowBook_WhenBookNotFound_ShouldThrowEntityNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            borrowingRecordService.borrowBook(1L, 1L);
        });
    }

    @Test
    void borrowBook_WhenPatronNotFound_ShouldThrowEntityNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            borrowingRecordService.borrowBook(1L, 1L);
        });
    }

    @Test
    void borrowBook_WhenBookNotAvailable_ShouldThrowIllegalStateException() {
        book.setStatus(BookStatus.BORROWED);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        assertThrows(IllegalStateException.class, () -> {
            borrowingRecordService.borrowBook(1L, 1L);
        });
    }

    @Test
    void borrowBook_WhenBookAndPatronExistAndBookIsAvailable_ShouldReturnBorrowingRecordDTO() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecordDTO result = borrowingRecordService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(BookStatus.BORROWED, book.getStatus());
    }

    @Test
    void returnBook_WhenNoActiveBorrowingRecordFound_ShouldThrowEntityNotFoundException() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            borrowingRecordService.returnBook(1L, 1L);
        });
    }

    @Test
    void returnBook_WhenActiveBorrowingRecordExists_ShouldReturnBorrowingRecordDTO() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(anyLong(), anyLong()))
                .thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecordDTO result = borrowingRecordService.returnBook(1L, 1L);

        assertNotNull(result);
        assertNotNull(result.getReturnDate());
        assertEquals(BookStatus.AVAILABLE, borrowingRecord.getBook().getStatus());
    }
}
