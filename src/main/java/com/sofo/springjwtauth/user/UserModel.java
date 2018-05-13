package com.sofo.springjwtauth.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by georges on 5/10/18.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity(name = "user_model")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String phone;
    private boolean hasValidSession;
}
