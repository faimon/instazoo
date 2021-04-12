package ru.faimon.instazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.faimon.instazoo.entity.Comment;
import ru.faimon.instazoo.entity.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}