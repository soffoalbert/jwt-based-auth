package com.sofo.springjwtauth;

import com.google.gson.Gson;
import com.sofo.springjwtauth.user.UserController;
import com.sofo.springjwtauth.user.UserModel;
import com.sofo.springjwtauth.user.UserNotFoundException;
import com.sofo.springjwtauth.user.UserRepository;
import com.sofo.springjwtauth.user.UserService;
import com.sofo.springjwtauth.user.UserVO;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserService userServiceMock;


    MockMvc mockMvcForLogout;
    MockMvc mockMvc;

    @PostConstruct
    public void init() {
        UserController userController = new UserController(userServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
        UserController logoutController = new UserController(userService);
        this.mockMvcForLogout = MockMvcBuilders.standaloneSetup(logoutController)
                .build();
    }

    @Test
    public void testSignUp() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setUsername("username");
        userVO.setPassword("password");
        userVO.setPhone("08388888");
        Gson gson = new Gson();
        String userVoJson = gson.toJson(userVO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userVoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignUpWithInvalidUser() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setUsername("username");
        userVO.setPassword("password");
        Gson gson = new Gson();
        String userVoJson = gson.toJson(userVO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userVoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRetrieveAllUsers() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setId(1);
        userVO.setUsername("username");
        userVO.setPassword("password");
        userVO.setPhone("08388888");
        userVO.setHasValidSession(true);

        Set<UserVO> userVOs = new HashSet<>();
        userVOs.add(userVO);

        Mockito.when(userService.retrieveAllUsers()).thenReturn(userVOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].phone").value(userVO.getPhone()));
    }

    @Test
    public void testRetrieveAllActiveUsers() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setId(1);
        userVO.setUsername("username");
        userVO.setPassword("password");
        userVO.setPhone("08388888");
        userVO.setHasValidSession(true);

        Set<UserVO> userVOs = new HashSet<>();
        userVOs.add(userVO);

        Mockito.when(userService.retrieveAllActiveUsers()).thenReturn(userVOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/active-users"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].phone").value(userVO.getPhone()));
    }

    @Test
    public void testLogout() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setUsername("username");
        userModel.setPassword("password");
        userModel.setPhone("08388888");

        userRepository.save(userModel);

        mockMvcForLogout.perform(MockMvcRequestBuilders.post("/api/logout/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        UserModel loggedOutUser = userRepository.findByUsername(userModel.getUsername());

        Assert.assertFalse(loggedOutUser.isHasValidSession());
    }

    @Test(expected = NestedServletException.class)
    public void testLogoutWithNonExistingUser() throws Exception {
        mockMvcForLogout.perform(MockMvcRequestBuilders.post("/api/logout/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
