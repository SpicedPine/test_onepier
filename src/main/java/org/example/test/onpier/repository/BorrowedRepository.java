package org.example.test.onpier.repository;

import org.example.test.onpier.model.Borrowed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowedRepository extends JpaRepository<Borrowed, Integer> {
}
