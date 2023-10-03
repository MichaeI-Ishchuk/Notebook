package com.service.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.notebook.dto.AddressDTO;
import com.service.notebook.dto.UserDTO;
import com.service.notebook.model.Address;
import com.service.notebook.model.User;
import com.service.notebook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    private User user;
    private UserDTO userDTO;
    private final Long userId = 22L;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(userId)
                .firstName("FirstName")
                .lastName("LastName")
                .email("user@domain.com")
                .birthday(new Date())
                .phoneNumber("+30123456789")
                .address(Address.builder()
                        .country("UK")
                        .city("London")
                        .buildingNumber(45)
                        .street("street")
                        .build())
                .build();

        userDTO = UserDTO.builder()
                .id(userId)
                .firstName("FirstName")
                .lastName("LastName")
                .email("user@domain.com")
                .birthday(new Date())
                .phoneNumber("+30123456789")
                .address(AddressDTO.builder()
                        .country("UK")
                        .city("London")
                        .buildingNumber(45)
                        .street("street")
                        .build())
                .build();
    }

    @Test
    void getAllUsersByDate_shouldReturnSuccessfulStatusAndListUser_WhenCorrectDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        when(userRepository.getAllUsersByDate(any(Date.class), any(Date.class))).thenReturn(List.of(user));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("fromDate", dateFormat.format(currentDate))
                        .param("toDate", dateFormat.format(currentDate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(userId));

    }

    @Test
    void getAllUsersByDate_shouldReturnBadRequestStatus_WhenIncorrectDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date toDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate);
        calendar.add(Calendar.DATE, +1);
        Date fromDate = calendar.getTime();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("fromDate", dateFormat.format(fromDate))
                        .param("toDate", dateFormat.format(toDate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(jsonPath("$.errorMessage").value("Incorrect input value fromDate and toDate"));
    }

    @Test
    void updateUser_shouldReturnSuccessfulStatus_WhenUpdateUser() throws Exception {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturnNotFoundStatus_WhenUpdateUser() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturnSuccessfulStatus_WhenDeleteUser() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturnNotFoundStatus_WhenDeleteUser() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchUser_shouldReturnSuccessfulStatus_WhenPatchUser() throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("firstName", "FirstName");
        objectMap.put("email", "user@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(objectMap)))
                .andExpect(status().isOk());
    }

    @Test
    void patchUser_shouldReturnNotFoundStatus_WhenPatchUser() throws Exception {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }
}
