package com.rbinternational.goit_demo_app.controller;

import com.rbinternational.goit_demo_app.model.Book;
import com.rbinternational.goit_demo_app.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookWebController.class)
public class BookWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(1L, "Test Book 1", "Test Author 1", "1234567890", 19.99, "Test Description 1", 2023);
        book2 = new Book(2L, "Test Book 2", "Test Author 2", "0987654321", 29.99, "Test Description 2", 2022);
    }

    @Test
    void getAllBooks_ShouldReturnBookListView() throws Exception {
        // Arrange
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", Arrays.asList(book1, book2)));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void showCreateForm_ShouldReturnBookFormView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-form"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void createBook_WithValidData_ShouldRedirectToBookList() throws Exception {
        // Arrange
        Book newBook = new Book(null, "New Book", "New Author", "1122334455", 15.99, "New Description", 2024);
        Book savedBook = new Book(3L, "New Book", "New Author", "1122334455", 15.99, "New Description", 2024);
        
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);

        // Act & Assert
        mockMvc.perform(post("/books")
                .param("title", "New Book")
                .param("author", "New Author")
                .param("isbn", "1122334455")
                .param("price", "15.99")
                .param("description", "New Description")
                .param("publicationYear", "2024"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void showEditForm_WithExistingId_ShouldReturnBookFormView() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

        // Act & Assert
        mockMvc.perform(get("/books/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-form"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book1));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void showEditForm_WithNonExistingId_ShouldRedirectToBookList() throws Exception {
        // Arrange
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/books/edit/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).getBookById(99L);
    }

    @Test
    void updateBook_WithValidData_ShouldRedirectToBookList() throws Exception {
        // Arrange
        Book updatedBook = new Book(1L, "Updated Book", "Updated Author", "1234567890", 24.99, "Updated Description", 2023);
        
        when(bookService.saveBook(any(Book.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(post("/books/update")
                .param("id", "1")
                .param("title", "Updated Book")
                .param("author", "Updated Author")
                .param("isbn", "1234567890")
                .param("price", "24.99")
                .param("description", "Updated Description")
                .param("publicationYear", "2023"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void deleteBook_WithExistingId_ShouldRedirectToBookList() throws Exception {
        // Arrange
        doNothing().when(bookService).deleteBook(1L);

        // Act & Assert
        mockMvc.perform(get("/books/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void viewBook_WithExistingId_ShouldReturnBookDetailsView() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

        // Act & Assert
        mockMvc.perform(get("/books/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-details"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book1));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void viewBook_WithNonExistingId_ShouldRedirectToBookList() throws Exception {
        // Arrange
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/books/view/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).getBookById(99L);
    }

    @Test
    void searchBooks_ByTitle_ShouldReturnMatchingBooks() throws Exception {
        // Arrange
        when(bookService.findBooksByTitle("Test")).thenReturn(Arrays.asList(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/books/search")
                .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", Arrays.asList(book1, book2)));

        verify(bookService, times(1)).findBooksByTitle("Test");
    }

    @Test
    void searchBooks_ByAuthor_ShouldReturnMatchingBooks() throws Exception {
        // Arrange
        when(bookService.findBooksByAuthor("Author 1")).thenReturn(Arrays.asList(book1));

        // Act & Assert
        mockMvc.perform(get("/books/search")
                .param("author", "Author 1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", Arrays.asList(book1)));

        verify(bookService, times(1)).findBooksByAuthor("Author 1");
    }
}
