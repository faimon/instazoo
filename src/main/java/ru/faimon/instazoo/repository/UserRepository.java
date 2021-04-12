package ru.faimon.instazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.faimon.instazoo.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserName(String userName);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);
}
