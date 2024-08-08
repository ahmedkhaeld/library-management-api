package com.library.borrowing.entity;

import com.library.book.entity.Book;
import com.library.patron.entity.Patron;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "borrowing_records", indexes = {
        @Index(name = "idx_book_id", columnList = "book_id"),
        @Index(name = "idx_patron_id", columnList = "patron_id"),
        @Index(name = "idx_borrow_date", columnList = "borrow_date"),
        @Index(name = "idx_return_date", columnList = "return_date")
})
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @Column(name = "borrow_date", nullable = false)
    private LocalDateTime borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    public BorrowingRecord() {
        // Default constructor
    }

    public BorrowingRecord(Book book, Patron patron, LocalDateTime borrowDate, LocalDateTime dueDate) {
        this.book = book;
        this.patron = patron;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "id=" + id +
                ", book=" + (book != null ? book.getId() : null) +
                ", patron=" + (patron != null ? patron.getId() : null) +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}