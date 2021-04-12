package ru.faimon.instazoo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.faimon.instazoo.dto.CommentDTO;
import ru.faimon.instazoo.entity.Comment;
import ru.faimon.instazoo.entity.Post;
import ru.faimon.instazoo.entity.User;
import ru.faimon.instazoo.exceptions.PostNotFoundException;
import ru.faimon.instazoo.repository.CommentRepository;
import ru.faimon.instazoo.repository.PostRepository;

import java.security.Principal;
import java.util.List;

@Service
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        User user = userService.getCurrentUser(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
        Comment comment = new Comment();
        comment.setMessage(commentDTO.getMessage());
        comment.setUsername(commentDTO.getUsername());
        comment.setPost(post);
        comment.setUserId(user.getId());
        LOG.info("Saving comment for Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for postId: " + postId));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .ifPresent(commentRepository::delete);
    }
}
