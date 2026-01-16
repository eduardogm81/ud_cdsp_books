package com.luv2code.books.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    private String title;
    private String author;
    private String category;
    private int rating;
}
