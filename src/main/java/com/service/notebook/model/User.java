package com.service.notebook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthday;
    private Address address;
    private String phoneNumber;
}
