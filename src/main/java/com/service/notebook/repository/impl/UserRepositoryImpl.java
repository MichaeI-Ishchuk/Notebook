package com.service.notebook.repository.impl;

import com.service.notebook.model.User;
import com.service.notebook.repository.UserRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Override
    public List<User> getAllUsersByDate(Date fromDate, Date toDate) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return user;
    }

    @Override
    public User saveOrUpdate(User user) {
        return user;
    }
}
