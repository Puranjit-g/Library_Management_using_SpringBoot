package org.jbd.JBD_MINOR1.Controller;

import jakarta.validation.Valid;
import org.jbd.JBD_MINOR1.Model.User;
import org.jbd.JBD_MINOR1.Service.UserService;
import org.jbd.JBD_MINOR1.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addStudent")
    public User addStudent(@RequestBody @Valid UserRequest userRequest){
        return userService.addStudent(userRequest);
    }

    @PostMapping("/addAdmin")
    public User addAdmin(@RequestBody @Valid UserRequest userRequest){
        return userService.addAdmin(userRequest);
    }

    @GetMapping("/addStudent")
    public User addStudent(){
        return null;
    }

    //When we have multiple filters(two filters on left, 2 operators, 2 values) and also taking
    //everything on string type so that i can take n no of parameters from postman
    @GetMapping("/filter")
    public List<User> filter(@RequestParam("filterBy") String filterBy,
                             @RequestParam("operator") String operator,
                             @RequestParam("values") String values){
        return userService.filter(filterBy , operator, values);

    }
}
