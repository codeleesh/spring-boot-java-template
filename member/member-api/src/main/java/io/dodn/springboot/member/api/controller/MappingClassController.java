package io.dodn.springboot.member.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {

    @GetMapping
    public String user() {

        return "get users";
    }

    @PostMapping
    public String addUser() {

        return "post user";
    }

    @GetMapping("/{userId}")
    public String findUser(final @PathVariable("userId") String userId) {

        return "get userId=" + userId;
    }

    @PatchMapping("/{userId}")
    public String updateUser(final @PathVariable("userId") String userId) {

        return "update userId=" + userId;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(final @PathVariable("userId") String userId) {

        return "delete userId=" + userId;
    }

}
