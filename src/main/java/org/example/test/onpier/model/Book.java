package org.example.test.onpier.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @Column(nullable = false)
    private String title;

    @Column
    private String author;

    @Column
    private String genre;

    @Column
    private String publisher;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Borrowed> borrowedHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;
        return Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
