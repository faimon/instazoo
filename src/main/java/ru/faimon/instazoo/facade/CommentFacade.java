package ru.faimon.instazoo.facade;

import org.springframework.stereotype.Service;
import ru.faimon.instazoo.dto.CommentDTO;
import ru.faimon.instazoo.entity.Comment;

@Service
public class CommentFacade {

    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setUsername(comment.getUsername());
        return commentDTO;
    }
}
