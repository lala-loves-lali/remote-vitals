package com.remote_vitals.backend.user.repositories;

import com.remote_vitals.backend.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByFirstNameAndLastName(String firstName, String lastName);
}
