package com.luv2code.books.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}
