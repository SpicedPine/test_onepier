package org.example.test.onpier.service;

import org.example.test.onpier.dto.UserDto;

import java.util.Date;
import java.util.List;

public interface UserService {

    List<UserDto> getAllUsersWithNotEmptyBorrowingHistory();

    List<UserDto> getAllUsersWhoBorrowedBookOnExactDate(Date date);
}
