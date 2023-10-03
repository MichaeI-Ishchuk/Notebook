package com.service.notebook.util.mapper;

import com.service.notebook.dto.AddressDTO;
import com.service.notebook.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address dtoToEntity(AddressDTO addressDTO);
    AddressDTO entityToDto(Address address);
}
