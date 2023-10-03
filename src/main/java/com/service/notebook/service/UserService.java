package com.service.notebook.service;

import com.service.notebook.dto.UserDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {
    boolean isUserAboveAge(Date birthday, int minAge);
    boolean isCorrectDate(Date fromDate, Date toDate);
    List<UserDTO> getAllUsersByDate(Date fromDate, Date toDate);
    void deleteUser(Long id);
    void patchUser(Long id, Map<String, Object> updates);
    void updateUser(Long id, UserDTO userDTO);
}
