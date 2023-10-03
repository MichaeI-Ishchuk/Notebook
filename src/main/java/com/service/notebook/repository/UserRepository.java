package com.service.notebook.repository;

import com.service.notebook.model.User;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    List<User> getAllUsersByDate(Date fromDate, Date toDate);
    void deleteById(Long id);
    Optional<User> findById(Long id);
    User save(User user);
    User saveOrUpdate(User user);
}
