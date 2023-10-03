package com.service.notebook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Address {
    private String country;
    private String city;
    private String street;
    private Integer buildingNumber;
}
