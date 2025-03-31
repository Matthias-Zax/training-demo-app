package com.rbinternational.goit_demo_app.controller;

import com.rbinternational.goit_demo_app.model.Book;
import com.rbinternational.goit_demo_app.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookWebController {

    private final BookService bookService;

    @Autowired
    public BookWebController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("book") Book book, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "book-form";
        }
        
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book created successfully!");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "book-form";
                })
                .orElse("redirect:/books");
    }

    @PostMapping("/update")
    public String updateBook(@Valid @ModelAttribute("book") Book book, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "book-form";
        }
        
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        return "redirect:/books";
    }

    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "book-details";
                })
                .orElse("redirect:/books");
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String author,
                              Model model) {
        if (title != null && !title.isEmpty()) {
            model.addAttribute("books", bookService.findBooksByTitle(title));
        } else if (author != null && !author.isEmpty()) {
            model.addAttribute("books", bookService.findBooksByAuthor(author));
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        return "book-list";
    }
}
