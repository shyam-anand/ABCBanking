package com.abcbank.banking.user.controllers;

import com.abcbank.banking.common.models.ApiResponse;
import com.abcbank.banking.user.models.UserInfo;
import com.abcbank.banking.user.models.UserDTO;
import com.abcbank.banking.user.servives.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         29/08/17
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService users;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody UserDTO userDTO) {
        UserInfo userInfo = users.create(userDTO);
        userDTO = new UserDTO(userInfo);
        ApiResponse response = new ApiResponse<>(userDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable("userId") Long userId) {
        UserInfo userInfo = users.get(userId);
        if (userInfo == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new ApiResponse<>(new UserDTO(userInfo)), HttpStatus.OK);
    }
}
