package com.service.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.notebook.dto.AddressDTO;
import com.service.notebook.dto.RegisterRequest;
import com.service.notebook.model.Address;
import com.service.notebook.model.User;
import com.service.notebook.repository.UserRepository;
import org.hamcrest.Matchers;
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
import java.util.Calendar;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    private User user;
    private RegisterRequest registerRequest;
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -18);
        Date date = calendar.getTime();

        registerRequest = RegisterRequest.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .email("user@domain.com")
                .birthday(date)
                .phoneNumber("+30123456789")
                .address(AddressDTO.builder()
                        .country("UK")
                        .city("London")
                        .buildingNumber(667)
                        .street("street")
                        .build())
                .build();
    }

    @Test
    void register_shouldReturnSuccessfulStatusAndUser_WhenCorrectRegisterDate() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location",
                        Matchers.containsString(""+user.getId())));
    }

    @Test
    void register_shouldReturnBadRequestStatus_WhenIncorrectBirthdayField() throws Exception {
        registerRequest.setBirthday(new Date());
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(jsonPath("$.errorMessage").value("Min age should be 18"));
    }

    @Test
    void register_shouldReturnBadRequestStatus_WhenInvalidFirstNameField() throws Exception {
        registerRequest.setFirstName("");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(jsonPath("$.errorMessage").value("firstName must not be blank"));
    }

    @Test
    void register_shouldReturnBadRequestStatus_WhenInvalidEmailField() throws Exception {
        registerRequest.setEmail("email");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("incorrectRequestData"))
                .andExpect(jsonPath("$.errorMessage").value("email must have correct format"));
    }
}
