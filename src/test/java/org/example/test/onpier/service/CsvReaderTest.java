package org.example.test.onpier.service;

import org.example.test.onpier.dto.BorrowedCsvDto;
import org.example.test.onpier.model.Book;
import org.example.test.onpier.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {


    @org.junit.jupiter.api.Test
    void readUsers() throws FileNotFoundException {
        List<User> users = CsvReader.readUsers();

        System.out.println(users);
    }

    @org.junit.jupiter.api.Test
    void readBooks() throws FileNotFoundException {
        Set<Book> books = CsvReader.readBooks();

        System.out.println(books);
    }

    @org.junit.jupiter.api.Test
    void readBorrowed() throws FileNotFoundException {
        List<BorrowedCsvDto> borrowedCsvDtoList = CsvReader.readBorrowed();

        System.out.println(borrowedCsvDtoList);
    }
}