package com.sofo.springjwtauth.auth;

import com.sofo.springjwtauth.user.UserModel;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by georges on 5/10/18.
 */
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AuthModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    @OneToOne
    private UserModel userModel;

    /**
     * Instantiates a new Auth model.
     *
     * @param token     the token
     * @param userModel the user model
     */
    public AuthModel(String token, UserModel userModel) {
        this.token = token;
        this.userModel = userModel;
    }
}
