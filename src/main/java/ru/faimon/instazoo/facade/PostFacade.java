package ru.faimon.instazoo.facade;

import org.springframework.stereotype.Service;
import ru.faimon.instazoo.dto.PostDTO;
import ru.faimon.instazoo.entity.Post;

@Service
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setCaption(post.getCaption());
        postDTO.setId(post.getId());
        postDTO.setLikes(postDTO.getLikes());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());
        postDTO.setUsersLiked(post.getLikedUsers());
        return postDTO;
    }
}
