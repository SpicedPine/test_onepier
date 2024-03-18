package org.example.test.onpier.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserId implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "first_name")
    private String firstName;

    @Override
    public String toString() {
        return "UserId{" +
                "name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
