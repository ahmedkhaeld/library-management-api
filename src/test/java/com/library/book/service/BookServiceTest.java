package com.library.book.service;

import com.library.book.dto.BookDTO;
import com.library.book.entity.Book;
import com.library.book.enums.BookStatus;
import com.library.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_ShouldReturnListOfBookDTOs() {
        // Arrange
        Book book1 = new Book("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "Description 1", BookStatus.AVAILABLE);
        Book book2 = new Book("Book 2", "Author 2", LocalDate.now(), "ISBN-2", "Description 2", BookStatus.BORROWED);
        List<Book> books = Arrays.asList(book1, book2);

        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Book 1", result.get(0).getTitle());
        Assertions.assertEquals("Book 2", result.get(1).getTitle());
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBookDTO() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "Description 1", BookStatus.AVAILABLE);
        book.setId(bookId); // Set the id here
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        BookDTO result = bookService.getBookById(bookId);

        // Assert
        Assertions.assertEquals(bookId, result.getId());
        Assertions.assertEquals("Book 1", result.getTitle());
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void createBook_WhenISBNIsUnique_ShouldCreateAndReturnBookDTO() {
        // Arrange
        BookDTO bookDTO = new BookDTO("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "AVAILABLE");
        Book book = new Book("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "Description 1", BookStatus.AVAILABLE);
        Mockito.when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(false);
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        // Act
        BookDTO result = bookService.createBook(bookDTO);

        // Assert
        Assertions.assertEquals(bookDTO.getTitle(), result.getTitle());
        Assertions.assertEquals(bookDTO.getIsbn(), result.getIsbn());
    }

    @Test
    void createBook_WhenISBNIsNotUnique_ShouldThrowDataIntegrityViolationException() {
        // Arrange
        BookDTO bookDTO = new BookDTO("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "AVAILABLE");
        Mockito.when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookService.createBook(bookDTO));
    }

    @Test
    void updateBook_WhenBookExists_ShouldUpdateAndReturnBookDTO() {
        // Arrange
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO("Updated Book", "Updated Author", LocalDate.now(), "ISBN-1", "BORROWED");
        Book existingBook = new Book("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "Description 1", BookStatus.AVAILABLE);
        existingBook.setId(bookId); // Set the id here

        // Create an updated version of the existingBook with the values from bookDTO
        Book updatedBook = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublicationDate(), bookDTO.getIsbn(), bookDTO.getDescription(), BookStatus.valueOf(bookDTO.getStatus()));
        updatedBook.setId(bookId); // Set the id here

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(updatedBook);

        // Act
        BookDTO result = bookService.updateBook(bookId, bookDTO);

        // Assert
        Assertions.assertEquals(bookDTO.getTitle(), result.getTitle());
        Assertions.assertEquals(bookDTO.getStatus(), result.getStatus());
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO("Updated Book", "Updated Author", LocalDate.now(), "ISBN-1", "BORROWED");
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(bookId, bookDTO));
    }

    @Test
    void updateBook_WhenIsbnNotUnique_ShouldThrowDataIntegrityViolationException() {
        // Arrange
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO("Updated Book", "Updated Author", LocalDate.now(), "ISBN-2", "BORROWED");
        Book existingBook = new Book("Book 1", "Author 1", LocalDate.now(), "ISBN-1", "Description 1", BookStatus.AVAILABLE);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        Mockito.when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookService.updateBook(bookId, bookDTO));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteTheBook() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.deleteBook(bookId);

        // Assert
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(bookId));
    }
}