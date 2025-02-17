package com.dinhlap.ims.controllers.user;

import com.dinhlap.ims.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList() {
        return "contents/user/user-list";
    }

    @GetMapping("/add")
    public String addUser() {
        return "contents/user/user-create";
    }

    @GetMapping("detail/{id}")
    public String userDetail(@PathVariable("id") Long id) {
        return "contents/user/user-detail";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id) {
        return "contents/user/user-edit";
    }

}
