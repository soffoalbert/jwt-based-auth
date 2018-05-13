package com.sofo.springjwtauth;

import com.sofo.springjwtauth.user.UserModel;
import com.sofo.springjwtauth.user.UserRepository;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByName() {
        UserModel userModel = new UserModel();
        userModel.setUsername("test-user");
        userModel.setPassword("password");
        userModel.setPhone("08388888");

        userRepository.save(userModel);

        UserModel foundUser = userRepository.findByUsername(userModel.getUsername());

        Assert.assertTrue(foundUser.equals(userModel));
    }

    @Test
    public void testSaveUser() {
        UserModel userModel = new UserModel();
        userModel.setUsername("test-user-2");
        userModel.setPassword("password");
        userModel.setPhone("08388888");

        UserModel savedUser = userRepository.save(userModel);

        Assert.assertTrue(userModel.equals(savedUser));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreateInvalidUser() {
        UserModel userModel = new UserModel();
        userModel.setUsername("username");
        userModel.setPhone("08388888");

        userRepository.save(userModel);
    }
}
