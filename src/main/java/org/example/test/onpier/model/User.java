package org.example.test.onpier.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER_TABLE")
public class User {

    @EmbeddedId
    private UserId id;

    @Column
    private Date memberSince;

    @Column
    private Date memberTill;

    @Column
    private String gender;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Borrowed> borrowingHistory;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", memberSince=" + memberSince +
                ", memberTill=" + memberTill +
                ", gender='" + gender + '\'' +
                ", borrowedList=" + borrowingHistory +
                '}';
    }

}
