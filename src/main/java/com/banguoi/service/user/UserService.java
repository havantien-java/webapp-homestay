package com.banguoi.service.user;

import com.banguoi.model.User;

public interface UserService {

    User findUserByName(String name);

    Iterable<User> findAll();

    User findById(Long id);

    void save(User user);

    void remove(Long id);
}