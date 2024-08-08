package com.library.book.entity;

import com.library.book.enums.BookStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    public Book() {
        // Default constructor
    }

    public Book(String title, String author, LocalDate publicationDate, String isbn, String description, BookStatus status) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
