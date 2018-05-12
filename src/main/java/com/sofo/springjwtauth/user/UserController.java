package com.sofo.springjwtauth.user;

import com.sofo.springjwtauth.auth.LogoutVO;
import java.util.Set;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by georges on 5/10/18.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    @Inject
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Sign up.
     *
     * @param user the user
     */
    @PutMapping("/users")
    public void signUp(@RequestBody UserVO user) {
        userService.createUser(user);
    }

    /**
     * Retrieve all users.
     *
     * @return the response entity
     */
    @GetMapping("/users")
    public ResponseEntity<Set<UserVO>> retrieveAllUsers() {
        Set<UserVO> userVOs = userService.retrieveAllUsers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userVOs);
    }

    /**
     * Retrieve all active users.
     *
     * @return the response entity
     */
    @GetMapping("/active-users")
    public ResponseEntity<Set<UserVO>> retrieveAllActiveUsers() {
        Set<UserVO> userVOs = userService.retrieveAllActiveUsers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userVOs);
    }

    /**
     * Logout.
     *
     * ignored the <code>session_token</code>
     * because the logout mechanism implemented does not require it.
     *
     * @param id       the id
     */
    @PostMapping("/logout/{id}")
    public void logout(@PathVariable String id) {
        userService.logout(id);
    }
}
