package com.globant.projectdynamodb.controller;

import com.globant.projectdynamodb.model.User;
import com.globant.projectdynamodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/dynamo/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /*@GetMapping
    List<User> getAllUsers(){
        User u = new User("1", "raul.maynar@globant.com", "raúl", "maynar");
        return Arrays.asList(u);
    }*/

    @GetMapping
    ResponseEntity<User> getUser(@RequestParam String id, @RequestParam String email){
        User user = userRepository.getUser(id, email);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping
    public String insert(@RequestBody User user){
        userRepository.insertIntoDynamoDB(user);
        return "User successfully inserted";
    }

    @PutMapping
    public void update(@RequestBody User user){
        userRepository.updateUser(user);
    }

    @DeleteMapping(value = "/{id}/{email}")
    public void delete(@PathVariable String id, @PathVariable String email){
        User userToDelete = new User();
        userToDelete.setId(id);
        userToDelete.setEmail(email);
        userRepository.deleteUser(userToDelete);
    }
}
