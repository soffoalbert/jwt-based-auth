package com.sofo.springjwtauth.user;

import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Component
public class UserMapper {

    /**
     * This method converts a <code>UserVO</code> to a <code>UserModel</code>.
     *
     * @param userVO the user vo
     * @return the user model
     */
    public UserModel toEntity(final UserVO userVO) {
        UserModel userModel = new UserModel();
        userModel.setHasValidSession(userVO.isHasValidSession());
        userModel.setPassword(userVO.getPassword());
        userModel.setPhone(userVO.getPhone());
        userModel.setUsername(userVO.getUsername());
        return userModel;
    }
}
