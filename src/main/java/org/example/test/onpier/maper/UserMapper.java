package org.example.test.onpier.maper;

import org.example.test.onpier.dto.UserDto;
import org.example.test.onpier.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static List<UserDto> mapModelTpDto(List<User> users){
        return users.stream().map(userTo -> UserDto.builder()
                .name(userTo.getId().getName())
                .firstName(userTo.getId().getFirstName())
                .memberSince(userTo.getMemberSince())
                .memberTill(userTo.getMemberTill())
                .gender(userTo.getGender()).build()).collect(Collectors.toList());
    }
}
