package com.service.notebook.util.mapper;

import com.service.notebook.dto.RegisterRequest;
import com.service.notebook.dto.UserDTO;
import com.service.notebook.model.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring",  uses = AddressMapper.class)
public interface UserMapper {
    User dtoToEntity(UserDTO userDTO);
    User registerDataToEntity(RegisterRequest user);
    UserDTO entityToDto(User user);
    List<UserDTO> dtoListToEntityList(List<User> userList);
}
