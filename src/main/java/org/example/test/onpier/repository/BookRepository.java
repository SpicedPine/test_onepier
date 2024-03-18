package org.example.test.onpier.repository;

import org.example.test.onpier.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
