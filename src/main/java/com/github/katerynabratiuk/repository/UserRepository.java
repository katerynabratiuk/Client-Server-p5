package com.github.katerynabratiuk.repository;

import com.github.katerynabratiuk.entity.User;

public interface UserRepository {
    User get(String login);
}
