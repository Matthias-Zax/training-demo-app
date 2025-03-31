package com.rbinternational.goit_demo_app.service;

import com.rbinternational.goit_demo_app.model.Book;
import com.rbinternational.goit_demo_app.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(1L, "Test Book 1", "Test Author 1", "1234567890", 19.99, "Test Description 1", 2023);
        book2 = new Book(2L, "Test Book 2", "Test Author 2", "0987654321", 29.99, "Test Description 2", 2022);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(book1, book2);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WithExistingId_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book1);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(3L);

        // Assert
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(3L);
    }

    @Test
    void saveBook_ShouldSaveAndReturnBook() {
        // Arrange
        Book newBook = new Book(null, "New Book", "New Author", "1122334455", 15.99, "New Description", 2024);
        Book savedBook = new Book(3L, "New Book", "New Author", "1122334455", 15.99, "New Description", 2024);
        
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Book result = bookService.saveBook(newBook);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getTitle()).isEqualTo("New Book");
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    void deleteBook_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void findBooksByTitle_ShouldReturnMatchingBooks() {
        // Arrange
        when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<Book> result = bookService.findBooksByTitle("Test");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(book1, book2);
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    void findBooksByAuthor_ShouldReturnMatchingBooks() {
        // Arrange
        when(bookRepository.findByAuthorContainingIgnoreCase("Author 1")).thenReturn(Arrays.asList(book1));

        // Act
        List<Book> result = bookService.findBooksByAuthor("Author 1");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(book1);
        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("Author 1");
    }

    @Test
    void findBooksByIsbn_ShouldReturnMatchingBooks() {
        // Arrange
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Arrays.asList(book1));

        // Act
        List<Book> result = bookService.findBooksByIsbn("1234567890");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(book1);
        verify(bookRepository, times(1)).findByIsbn("1234567890");
    }
}
