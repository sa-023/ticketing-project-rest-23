package com.company.service.impl;
import com.company.dto.ProjectDTO;
import com.company.dto.TaskDTO;
import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.exception.TicketingProjectException;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.KeycloakService;
import com.company.service.ProjectService;
import com.company.service.TaskService;
import com.company.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final KeycloakService keycloakService;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ProjectService projectService, TaskService taskService, KeycloakService keycloakService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.keycloakService = keycloakService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(p-> userMapper.convertToDTO(p)).collect(Collectors.toList());
//        return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return userMapper.convertToDTO(user);
    }

    @Override
    public void save(UserDTO dto) {
        dto.setEnabled(true);
        String encodedPassword = passwordEncoder.encode(dto.getPassWord());
        User obj = userMapper.convertToEntity(dto);
        obj.setPassWord(encodedPassword);
        userRepository.save(obj);
        keycloakService.userCreate(dto); // Will save the user in keycloak automatically.
    }

    @Override
    public UserDTO update(UserDTO dto) {
        /*
         * 🖍️...
         * 1. Find the current user to capture the ID. Getting the user ID from the Database.
         * 2. Map updated userDto to entity object. Convert dto, which has no ID, to an entity (In the User Create view, there is no id field).
         * 3. Set the ID to the converted object (Set "convertedUser" id to "user" id).
         * 4. Save updated user (Save "convertedUser" in DB).
         */
        User user = userRepository.findByUserName(dto.getUserName()); // 1.
        User convertedUser = userMapper.convertToEntity(dto); // 2.
        convertedUser.setId(user.getId()); // 3.
        userRepository.save(convertedUser); // 4.
        return findByUserName(dto.getUserName());
    }


    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    @Override
    public void delete(String username) throws TicketingProjectException { // The user will not be deleted from the database; only the flag (isDeleted) value will be changed.
        User user = userRepository.findByUserName(username);
        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            // While the flag of the userName changes in DB, we add the "-" sign at the end of the userName, so we can reuse it to create a new user.
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }else {
            throw new TicketingProjectException("User can not be deleted");
        }
    }

    private boolean checkIfUserCanBeDeleted(User user) throws TicketingProjectException {
        if (user == null) {
            throw new TicketingProjectException("User not found");
        }
        switch (user.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.readAllByAssignedManager(user);
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.readAllByAssignedEmployee(user);
                return taskDTOList.size() == 0;
            default:
                return true;
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }





}
