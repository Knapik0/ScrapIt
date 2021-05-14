package com.example.scrapeit.exception;

public class FileOfGivenIdNotFoundException extends RuntimeException {
    public FileOfGivenIdNotFoundException(String s) {
        super(s);
    }
}
