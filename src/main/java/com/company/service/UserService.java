package com.company.service;
import com.company.dto.UserDTO;
import com.company.exception.TicketingProjectException;

import java.util.List;

public interface UserService{

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    UserDTO save(UserDTO dto);
    UserDTO update(UserDTO dto);
    void deleteByUserName(String username);
    void delete(String username) throws TicketingProjectException;
    List<UserDTO> listAllByRole(String role);





}
