package org.example.test.onpier.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BorrowedCsvDto {
    String borrowerName;
    String bookName;
    Date borrowedFrom;
    Date borrowedTo;
}
