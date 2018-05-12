package com.sofo.springjwtauth.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by georges on 5/10/18.
 */
public interface UserRepository extends CrudRepository<UserModel, Long> {
    /**
     * Find s a user by username.
     *
     * @param username the username
     * @return the user model
     */
    UserModel findByUsername(String username);

    /**
     * Finds distinct Users.
     *
     * @return the set
     */
    Set<UserModel> findAll();

    /**
     * Finds all active users.
     *
     * @return the set
     */
    @Query(value = "SELECT * FROM user_model u WHERE u.has_valid_session = true", nativeQuery = true)
    Set<UserModel> findAllActiveUsers();
}
