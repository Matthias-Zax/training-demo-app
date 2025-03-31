package com.rbinternational.goit_demo_app.config;

import com.rbinternational.goit_demo_app.model.Book;
import com.rbinternational.goit_demo_app.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Autowired
    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        // Add sample books if the database is empty
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book(null, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 12.99, 
                "A novel about the mysterious millionaire Jay Gatsby and his obsession with the beautiful Daisy Buchanan.", 1925));
                
            bookRepository.save(new Book(null, "To Kill a Mockingbird", "Harper Lee", "9780061120084", 14.99, 
                "A novel about racial injustice and moral growth in the American South during the 1930s.", 1960));
                
            bookRepository.save(new Book(null, "1984", "George Orwell", "9780451524935", 11.99, 
                "A dystopian novel set in a totalitarian society where critical thought is suppressed.", 1949));
                
            bookRepository.save(new Book(null, "The Hobbit", "J.R.R. Tolkien", "9780547928227", 15.99, 
                "A fantasy novel about the adventures of hobbit Bilbo Baggins.", 1937));
                
            bookRepository.save(new Book(null, "Pride and Prejudice", "Jane Austen", "9780141439518", 9.99, 
                "A romantic novel following the character development of Elizabeth Bennet.", 1813));
                
            bookRepository.save(new Book(null, "The Catcher in the Rye", "J.D. Salinger", "9780316769488", 10.99, 
                "A novel about teenage alienation and loss of innocence.", 1951));
        }
    }
}
