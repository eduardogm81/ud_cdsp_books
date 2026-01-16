package com.luv2code.books.controller;

import com.luv2code.books.entity.Book;
import com.luv2code.books.exception.BookNotFoundException;
import com.luv2code.books.request.BookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Book Rest API Endpoints", description = "Operations related to Books")
@RestController
@RequestMapping("/api/books")
public class BookController {


    private final List<Book> books = new ArrayList<>();

    public BookController() {
        this.initializeBooks();
    }

    private void initializeBooks() {
        books.addAll(List.of(
                new Book(1, "Computer Science Pro", "Chad Darby", "Computer Science", 5),
                new Book(2, "Java Srping Master", "Eric Roby", "Computer Science", 5),
                new Book(3, "Why 1+1 Rocks", "Adil A.", "Math", 5),
                new Book(4, "How Bears Hibernate", "Bob B.", "Science", 2),
                new Book(5, "A Pirate's Treasure", "Curt C.", "History", 3),
                new Book(6, "Why 2+2 is better", "Dan D.", "Math", 1)
        ));
    }

    @Operation(summary = "Get all books", description = "Retrieve a list of all aviable books")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks(
            @Parameter(description = "Optional query parameter") @RequestParam(required = false) String category
    ) {
        if (category == null) return books;
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookByTitle(
            @Parameter(description = "Id of the book to be retrieved") @PathVariable @Min(1) long id
    ) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    @Operation(summary = "Create a new book", description = "Add a new book to the collection")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBook(@Valid @RequestBody BookRequest bookRequest) {
        long id = getNextId();
        books.add(converToBook(id, bookRequest));
    }

    @Operation(summary = "Update an existing book", description = "Update the details of an existing book")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Book updateBook(
            @Parameter(description = "Id of the book to update") @PathVariable @Min(1) long id,
            @Valid @RequestBody BookRequest bookRequest
    ) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                Book updatedBook = converToBook(id, bookRequest);
                books.set(i, updatedBook);
                return updatedBook;
            }
        }

        throw new BookNotFoundException("Book not found with id: " + id);
    }

    @Operation(summary = "Delete a book", description = "Remove a book from the collection by its ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(
            @Parameter(description = "Id of the book to delete") @PathVariable @Min(1) long id
    ) {
        books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        books.removeIf(book -> book.getId() == id);
    }

    private long getNextId() {
        return books.stream().mapToLong(Book::getId).max().orElse(0L) + 1;
    }

    private Book converToBook(long id, BookRequest bookRequest) {
        return new Book(id, bookRequest.getTitle(), bookRequest.getAuthor(),
                bookRequest.getCategory(), bookRequest.getRating());
    }

}
