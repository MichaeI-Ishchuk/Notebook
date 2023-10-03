package com.service.notebook.service.impl;

import com.service.notebook.dto.RegisterRequest;
import com.service.notebook.dto.UserDTO;
import com.service.notebook.exception.IncorrectDateException;
import com.service.notebook.model.User;
import com.service.notebook.repository.UserRepository;
import com.service.notebook.service.UserService;
import com.service.notebook.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${user.min-age}")
    private int minAge;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDTO register(RegisterRequest user) {
        if (userService.isUserAboveAge(user.getBirthday(), minAge)) {
            User userEntity = userRepository.save(userMapper.registerDataToEntity(user));

            return userMapper.entityToDto(userEntity);
        } else {
            throw new IncorrectDateException("Min age should be 18");
        }
    }
}
