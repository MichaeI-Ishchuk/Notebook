package com.service.notebook.service.impl;

import com.service.notebook.dto.UserDTO;
import com.service.notebook.exception.EntityNotFoundException;
import com.service.notebook.exception.IncorrectDateException;
import com.service.notebook.model.User;
import com.service.notebook.repository.UserRepository;
import com.service.notebook.service.UserService;
import com.service.notebook.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String MESSAGE = "user is not found";


    @Override
    public List<UserDTO> getAllUsersByDate(Date fromDate, Date toDate) {
        if (isCorrectDate(fromDate, toDate)) {
          return userMapper.dtoListToEntityList(userRepository.getAllUsersByDate(fromDate, toDate));
        } else {
            throw new IncorrectDateException("Incorrect input value fromDate and toDate");
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MESSAGE));
        userRepository.deleteById(id);
    }

    @Override
    public void patchUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MESSAGE));

        updates.forEach((key, value)->{
           Field field = ReflectionUtils.findField(User.class, key);
           field.setAccessible(true);
           ReflectionUtils.setField(field, user, value);
        });

        userRepository.saveOrUpdate(user);
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MESSAGE));
        userRepository.saveOrUpdate(userMapper.dtoToEntity(userDTO));
    }

    @Override
    public boolean isUserAboveAge(Date birthday, int minAge) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthLocalDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(birthLocalDate, currentDate);
        int age = period.getYears();

        return age >= minAge;
    }

    @Override
    public boolean isCorrectDate(Date fromDate, Date toDate) {
        return !fromDate.after(toDate);
    }
}
