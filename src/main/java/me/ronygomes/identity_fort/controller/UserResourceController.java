package me.ronygomes.identity_fort.controller;

import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/users")
public class UserResourceController {

    private final UserRepository userRepository;

    @Autowired
    public UserResourceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/info")
    public User fetchInfo(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow(IllegalAccessError::new);
    }

}
