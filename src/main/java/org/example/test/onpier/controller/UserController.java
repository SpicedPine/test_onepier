package org.example.test.onpier.controller;

import lombok.Getter;
import org.example.test.onpier.dto.UserDto;
import org.example.test.onpier.model.User;
import org.example.test.onpier.service.UserService;
import org.example.test.onpier.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@Getter
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsersWithHistory")
    public ResponseEntity<List<UserDto>> getAllUsersWithBorrowingHistory(){
        List<UserDto> allUsersWithNotEmptyBorrowingHistory = userService.getAllUsersWithNotEmptyBorrowingHistory();

        return allUsersWithNotEmptyBorrowingHistory.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allUsersWithNotEmptyBorrowingHistory);
    }

    @GetMapping("/allUsersBorrowedOnSpecificDate")
    public ResponseEntity<List<UserDto>> getAllUsersWithBorrowingHistory(@RequestParam(name = "dateOfBorrowing") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        List<UserDto> allUsersWithNotEmptyBorrowingHistory = userService.getAllUsersWhoBorrowedBookOnExactDate(date);

        return allUsersWithNotEmptyBorrowingHistory.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allUsersWithNotEmptyBorrowingHistory);
    }
}
