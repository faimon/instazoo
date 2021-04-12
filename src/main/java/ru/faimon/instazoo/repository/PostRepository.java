package ru.faimon.instazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.faimon.instazoo.entity.Post;
import ru.faimon.instazoo.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserOrderByCreatedDateDesc(User user);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);
}
