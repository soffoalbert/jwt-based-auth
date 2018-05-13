package com.sofo.springjwtauth.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * Instantiates a new User service.
     *
     * @param userRepository        the user repository
     * @param userMapper            the user mapper
     * @param bCryptPasswordEncoder the b crypt password encoder
     */
    public UserService(UserRepository userRepository, final UserMapper userMapper,
                       final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Creates a new user.
     *
     * @param userVO the user vo
     */
    public void createUser(final UserVO userVO) {
        userVO.setPassword(bCryptPasswordEncoder.encode(userVO.getPassword()));
        userVO.setHasValidSession(true);
        UserModel userModel = userMapper.toEntity(userVO);
        userRepository.save(userModel);
    }

    /**
     * Retrieve s all users.
     *
     * @return the set
     */
    public Set<UserVO> retrieveAllUsers() {
        Set<UserVO> userVOs = new HashSet<>();
        userRepository.findAll().forEach(userModel ->
                userVOs.add(new UserVO(userModel.getId(), userModel.getUsername(),
                        userModel.getPassword(), userModel.getPhone()))
        );
        return userVOs;
    }

    /**
     * Retrieve s all active users.
     *
     * @return the set
     */
    public Set<UserVO> retrieveAllActiveUsers() {
        Set<UserVO> userVOs = new HashSet<>();
        userRepository.findAllActiveUsers().forEach(userModel ->
                userVOs.add(new UserVO(userModel.getId(), userModel.getUsername(),
                        userModel.getPassword(), userModel.getPhone()))
        );
        return userVOs;
    }

    /**
     * Logout.
     *
     * @param id the id
     */
    public void logout(String id) throws UserNotFoundException {
        Optional<UserModel> userModel = userRepository.findById(Long.parseLong(id));
        if (!userModel.isPresent()) {
            throw new UserNotFoundException("The user you are trying to logout does not exist");
        }
        userModel.get().setHasValidSession(false);
        userRepository.save(userModel.get());
    }
}
