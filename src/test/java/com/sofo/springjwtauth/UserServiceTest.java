package com.sofo.springjwtauth;

import com.sofo.springjwtauth.user.UserMapper;
import com.sofo.springjwtauth.user.UserModel;
import com.sofo.springjwtauth.user.UserRepository;
import com.sofo.springjwtauth.user.UserService;
import com.sofo.springjwtauth.user.UserVO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, userMapper, bCryptPasswordEncoder);
    }

    @Test
    public void testSignUp() {
        UserVO userVO = new UserVO();
        userVO.setUsername("username");
        userVO.setPassword("password");
        userVO.setPhone("08388888");

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setUsername("username");
        userModel.setPassword("password");
        userModel.setPhone("08388888");

        Mockito.when(userMapper.toEntity(userVO)).thenReturn(userModel);

        userService.createUser(userVO);

        Mockito.verify(userRepository).save(userModel);
        Mockito.verify(bCryptPasswordEncoder).encode(userModel.getPassword());
        Mockito.verify(userMapper).toEntity(userVO);


    }

    @Test
    public void testRetrieveAllActiveUsers() {

        UserVO userVO = new UserVO();
        userVO.setId(1);
        userVO.setUsername("username");
        userVO.setPassword("password");
        userVO.setPhone("08388888");
        userVO.setHasValidSession(true);

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setUsername("username");
        userModel.setPassword("password");
        userModel.setPhone("08388888");
        userModel.setHasValidSession(true);

        Set<UserModel> userModels = new HashSet<>();
        userModels.add(userModel);

        Mockito.when(userRepository.findAllActiveUsers()).thenReturn(userModels);

        Set<UserVO> userVOS = userService.retrieveAllActiveUsers();
        Assert.assertEquals(new ArrayList<>(userVOS).get(0).getPassword(), userModel.getPassword());
        Assert.assertEquals(new ArrayList<>(userVOS).get(0).getPhone(), userModel.getPhone());
        Assert.assertEquals(new ArrayList<>(userVOS).get(0).getUsername(), userModel.getUsername());
    }

    @Test
    public void testRetrieveAllUsers() {
        UserVO userVO = new UserVO();
        userVO.setId(1);
        userVO.setUsername("username");
        userVO.setPassword("password");
        userVO.setPhone("08388888");
        userVO.setHasValidSession(true);

        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setUsername("username");
        userModel.setPassword("password");
        userModel.setPhone("08388888");
        userModel.setHasValidSession(true);

        Set<UserModel> userModels = new HashSet<>();
        userModels.add(userModel);

        Mockito.when(userRepository.findAll()).thenReturn(userModels);

        Set<UserVO> userVOS = userService.retrieveAllUsers();
        Assert.assertEquals(new ArrayList<>(userVOS).get(0).getPassword(), userModel.getPassword());
        Assert.assertEquals(new ArrayList<>(userVOS).get(0).getPhone(), userModel.getPhone());
        Assert.assertEquals(new ArrayList<>(userVOS).get(0).getUsername(), userModel.getUsername());
    }

    @Test
    public void testLogout() {
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setUsername("username");
        userModel.setPassword("password");
        userModel.setPhone("08388888");
        userModel.setHasValidSession(true);

        Optional<UserModel> userModel1 = Optional.of(userModel);

        Mockito.when(userRepository.findById(1L)).thenReturn(userModel1);

        userService.logout("1");

        Mockito.verify(userRepository).save(userModel);
    }
}
