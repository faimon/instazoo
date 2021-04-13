package ru.faimon.instazoo.facade;

import org.springframework.stereotype.Component;
import ru.faimon.instazoo.dto.UserDTO;
import ru.faimon.instazoo.entity.User;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setBio(user.getBio());
        return userDTO;
    }
}
