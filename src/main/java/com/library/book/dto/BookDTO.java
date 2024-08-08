package com.library.book.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author must be at most 255 characters")
    private String author;

    @NotNull(message = "Publication date is required")
    @PastOrPresent(message = "Publication date must be in the past or present")
    private LocalDate publicationDate;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?[0-9X-]{13}$", message = "Invalid ISBN format")
    private String isbn;


    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Status is required")
    @Pattern(regexp = "AVAILABLE|BORROWED|LOST|UNDER_MAINTENANCE",
            message = "Status must be AVAILABLE, BORROWED, LOST, or UNDER_MAINTENANCE")
    private String status;


    public BookDTO() {}

    public BookDTO(String title, String author, LocalDate publicationDate, String isbn, String status) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.status = status;
    }


    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}