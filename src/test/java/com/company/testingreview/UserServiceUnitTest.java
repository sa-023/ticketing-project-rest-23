package com.company.testingreview;
import com.company.dto.RoleDTO;
import com.company.dto.UserDTO;
import com.company.entity.Role;
import com.company.entity.User;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.KeycloakService;
import com.company.service.ProjectService;
import com.company.service.TaskService;
import com.company.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.NoSuchElementException;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/*
 * 🖍️...
 * · The stub is an interface that has a minimum count of methods to simulate the actual object.
 *   It is an object that has preexisting data and returns a fixed value irrespective of input.
 *
 * · While the @Mock annotation creates a complete mock, the @Spy annotation creates a "partial mock."
 *   With a partial mock, you can mock only specific methods of a real object, while other methods will retain their original behavior.
 *   When we spy on an object, the real methods are being called unless it's stubbed
 *
 * · @InjectMocks: It creates an instance of the class and injects the mocks that are marked with the annotation @Mock into it.
 * · AssertJ: It is a Java library that provides writing assertions.
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    @Spy // means this is the real object; spy on it to see if it has been used or called.
    private UserMapper userMapper = new UserMapper(new ModelMapper());
    User user;
    UserDTO userDTO;

    @BeforeEach
    void setUp(){
        user = new User(); // entity
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO(); // dto
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");
        userDTO.setRole(roleDTO);
    }

    private List<User> getUsers(){ // Test data, User list
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");
        return List.of(user,user2);
    }
    private List<UserDTO> getUserDTOs(){ // Test data UserDTo list
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        return List.of(userDTO,userDTO2);
    }





    @Test
    void should_list_all_users(){
        //stub: When the method returns something, we use a stub
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers());
        List<UserDTO> actualList = userService.listAllUsers();
        List<UserDTO> expectedList = getUserDTOs();
        //Assertions.assertEquals(expectedList,actualList); // This compares the object references.
        //AssertJ
        assertThat(actualList).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedList); // This compares the object values.
    }

    @Test
    void should_find_user_by_username(){
        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(user);
        // Lenient stubs bypass “strict stubbing” validation rules. Ex: When stubbing is declared as lenient, it won’t be checked for potential stubbing problems.
        //lenient().when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers()); /
        UserDTO actualUserDTO = userService.findByUserName("user");
        UserDTO expectedUserDTO = userDTO;
        assertThat(actualUserDTO).usingRecursiveComparison().isEqualTo(expectedUserDTO);
    }

    @Test
    void should_throw_exception_when_user_not_found(){
        // We can either stub null or do nothing since it will return null as well.
        //when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(null);
        // We call the method and capture the exception and its message.
        Throwable throwable = catchThrowable(()->userService.findByUserName("someUsername"));
        // We use the assertInstanceOf method to verify the exception type.
        assertInstanceOf(NoSuchElementException.class,throwable);
        // We can verify exception messages with assertEquals.
        assertEquals("User not found",throwable.getMessage());
    }

    @Test
    void should_save_user(){
        when(passwordEncoder.encode(anyString())).thenReturn("anypassword");
        when(userRepository.save(any())).thenReturn(user);
        UserDTO actualDTO = userService.save(userDTO);
        verify(keycloakService).userCreate(any()); // When the method does not return anything, we just use verify.
        assertThat(actualDTO).usingRecursiveComparison().isEqualTo(userDTO);
    }

















}
