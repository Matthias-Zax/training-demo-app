package com.rbinternational.goit_demo_app.controller;

import com.rbinternational.goit_demo_app.model.Book;
import com.rbinternational.goit_demo_app.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.getBookById(id)
                .map(existingBook -> {
                    book.setId(id);
                    return ResponseEntity.ok(bookService.saveBook(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> {
                    bookService.deleteBook(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> findBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.findBooksByTitle(title));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> findBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.findBooksByAuthor(author));
    }

    @GetMapping("/search/isbn")
    public ResponseEntity<List<Book>> findBooksByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(bookService.findBooksByIsbn(isbn));
    }
}
