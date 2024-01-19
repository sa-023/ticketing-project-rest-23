package com.company.controller;
import com.company.dto.UserDTO;
import com.company.entity.ResponseWrapper;
import com.company.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved",userDTOList, HttpStatus.OK)); // OK -> 200
    }

    @GetMapping("/{userName}")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("userName") String userName){
        UserDTO user = userService.findByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully retrieved",user,HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user){
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is successfully created",HttpStatus.CREATED)); // CREATED -> 201
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user){
        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully updated",user,HttpStatus.OK));
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("userName") String userName){
        userService.deleteByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully deleted",HttpStatus.OK));
//      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseWrapper("User is successfully created",HttpStatus.CREATED));
        /*
         * 🖍️...
         * · 204 -> HttpStatus.NO_CONTENT
         * · If the request is successful, HttpStatus.NO_CONTENT will return a success message without a response body.
         *   Because of that, we use HttpStatus.OK to get a ResponseWrapper message.
         */
    }















}