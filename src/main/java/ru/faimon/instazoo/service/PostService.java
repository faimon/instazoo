package ru.faimon.instazoo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.faimon.instazoo.dto.PostDTO;
import ru.faimon.instazoo.entity.ImageModel;
import ru.faimon.instazoo.entity.Post;
import ru.faimon.instazoo.entity.User;
import ru.faimon.instazoo.exceptions.PostNotFoundException;
import ru.faimon.instazoo.repository.ImageRepository;
import ru.faimon.instazoo.repository.PostRepository;
import ru.faimon.instazoo.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    public PostService(UserRepository userRepository, PostRepository postRepository, ImageRepository imageRepository, UserService userService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = userService.getCurrentUser(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setTitle(postDTO.getTitle());
        post.setLocation(postDTO.getLocation());
        post.setLikes(0L);
        LOG.info("Saving Post for User: {} ", user);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = userService.getCurrentUser(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
    }

    public List<Post> getAllPostsByUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for postId: " + postId));
        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(user -> user.equals(username)).findFirst();
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }
}
