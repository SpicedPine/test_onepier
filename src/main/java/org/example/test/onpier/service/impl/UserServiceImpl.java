package org.example.test.onpier.service.impl;

import lombok.Getter;
import org.example.test.onpier.dto.UserDto;
import org.example.test.onpier.maper.UserMapper;
import org.example.test.onpier.model.User;
import org.example.test.onpier.repository.UserRepository;
import org.example.test.onpier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Getter
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsersWithNotEmptyBorrowingHistory(){
        List<User> usersWithBorrowingHistory = userRepository.findUsersWithNonEmptyBorrowingHistory();
        List<UserDto> userDtos = UserMapper.mapModelTpDto(usersWithBorrowingHistory);
        return userDtos;
    }

    @Override
    public List<UserDto> getAllUsersWhoBorrowedBookOnExactDate(Date dateOfBorrow) {
        List<User> usersBorrowedBookedOnCertainDate = userRepository.findUsersBorrowedBookedOnCertainDate(dateOfBorrow);
        List<UserDto> userDtos = UserMapper.mapModelTpDto(usersBorrowedBookedOnCertainDate);
        return userDtos;
    }
}
