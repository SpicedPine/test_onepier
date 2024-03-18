package org.example.test.onpier.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BORROWED")
public class Borrowed {

    @Id
    @GeneratedValue
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "name", referencedColumnName = "name"),
            @JoinColumn(name = "firstName", referencedColumnName = "first_name")
    })
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_title", referencedColumnName = "title",nullable = false)
    private Book book;

    @Column
    Date borrowedFrom;

    @Column
    Date borrowedTo;
}
