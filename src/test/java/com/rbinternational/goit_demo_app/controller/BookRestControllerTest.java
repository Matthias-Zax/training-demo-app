package com.rbinternational.goit_demo_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbinternational.goit_demo_app.model.Book;
import com.rbinternational.goit_demo_app.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(1L, "Test Book 1", "Test Author 1", "1234567890", 19.99, "Test Description 1", 2023);
        book2 = new Book(2L, "Test Book 2", "Test Author 2", "0987654321", 29.99, "Test Description 2", 2022);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() throws Exception {
        // Arrange
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test Book 2")));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_WithExistingId_ShouldReturnBook() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Book 1")))
                .andExpect(jsonPath("$.author", is("Test Author 1")));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getBookById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(99L);
    }

    @Test
    void createBook_WithValidData_ShouldReturnCreatedBook() throws Exception {
        // Arrange
        Book newBook = new Book(null, "New Book", "New Author", "1122334455", 15.99, "New Description", 2024);
        Book savedBook = new Book(3L, "New Book", "New Author", "1122334455", 15.99, "New Description", 2024);
        
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("New Author")));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void updateBook_WithExistingIdAndValidData_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        Book updatedBook = new Book(1L, "Updated Book", "Updated Author", "1234567890", 24.99, "Updated Description", 2023);
        
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.saveBook(any(Book.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Book")))
                .andExpect(jsonPath("$.author", is("Updated Author")));

        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void updateBook_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        Book updatedBook = new Book(99L, "Updated Book", "Updated Author", "1234567890", 24.99, "Updated Description", 2023);
        
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/books/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(99L);
        verify(bookService, never()).saveBook(any(Book.class));
    }

    @Test
    void deleteBook_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        doNothing().when(bookService).deleteBook(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void deleteBook_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(99L);
        verify(bookService, never()).deleteBook(anyLong());
    }

    @Test
    void findBooksByTitle_ShouldReturnMatchingBooks() throws Exception {
        // Arrange
        when(bookService.findBooksByTitle("Test")).thenReturn(Arrays.asList(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/api/books/search/title?title=Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(jsonPath("$[1].title", is("Test Book 2")));

        verify(bookService, times(1)).findBooksByTitle("Test");
    }

    @Test
    void findBooksByAuthor_ShouldReturnMatchingBooks() throws Exception {
        // Arrange
        when(bookService.findBooksByAuthor("Author 1")).thenReturn(Arrays.asList(book1));

        // Act & Assert
        mockMvc.perform(get("/api/books/search/author?author=Author 1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("Test Author 1")));

        verify(bookService, times(1)).findBooksByAuthor("Author 1");
    }

    @Test
    void findBooksByIsbn_ShouldReturnMatchingBooks() throws Exception {
        // Arrange
        when(bookService.findBooksByIsbn("1234567890")).thenReturn(Arrays.asList(book1));

        // Act & Assert
        mockMvc.perform(get("/api/books/search/isbn?isbn=1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is("1234567890")));

        verify(bookService, times(1)).findBooksByIsbn("1234567890");
    }
}
