package com.github.katerynabratiuk.service;

import com.github.katerynabratiuk.entity.User;

public interface UserService {
    User get(String login);
}
