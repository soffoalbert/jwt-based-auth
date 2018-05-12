package com.sofo.springjwtauth.auth;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by georges on 5/10/18.
 */
public interface AuthRepository extends JpaRepository<AuthModel, Long> {
}
