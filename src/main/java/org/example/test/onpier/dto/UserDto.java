package org.example.test.onpier.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDto {
    private String name;
    private String firstName;
    private Date memberSince;
    private Date memberTill;
    private String gender;
}
