package com.github.katerynabratiuk.service.implementation;

import com.github.katerynabratiuk.entity.User;
import com.github.katerynabratiuk.repository.UserRepository;
import com.github.katerynabratiuk.repository.implementation.UserRepositoryImpl;

public class UserServiceImpl implements UserRepository {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User get(String login) {
        return userRepository.get(login);
    }
}
