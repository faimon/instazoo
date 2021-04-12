package ru.faimon.instazoo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.faimon.instazoo.entity.ImageModel;
import ru.faimon.instazoo.entity.Post;
import ru.faimon.instazoo.entity.User;
import ru.faimon.instazoo.exceptions.ImageNotFoundException;
import ru.faimon.instazoo.exceptions.PostNotFoundException;
import ru.faimon.instazoo.repository.ImageRepository;
import ru.faimon.instazoo.repository.PostRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    public ImageUploadService(ImageRepository imageRepository, UserService userService, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public ImageModel uploadImageForUser(MultipartFile file, Principal principal) throws IOException {
        User user = userService.getCurrentUser(principal);
        LOG.info("Uploading image for user : " + user.getUsername());
        imageRepository.findByUserId(user.getId())
                .ifPresent(imageRepository::delete);
        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        return imageRepository.save(imageModel);
    }

    public ImageModel uploadImageForPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = userService.getCurrentUser(principal);
        LOG.info("Uploading image for postId : " + postId);
        Post post = user.getPosts().stream()
                .filter(post1 -> post1.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException("Cannot found post with postId: " + postId));
        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setPostId(post.getId());
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        return imageRepository.save(imageModel);
    }

    public ImageModel getImageForUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        ImageModel imageModel = imageRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ImageNotFoundException("Image not found for user: " + user.getUsername()));
        deCompressBytes(imageModel.getImageBytes());
        return imageModel;
    }

    public ImageModel getImageForPost(Long postId) {
        ImageModel imageModel = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Image not found for postId: " + postId));
        deCompressBytes(imageModel.getImageBytes());
        return imageModel;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            LOG.info("Compressed Image Byte size = " + outputStream.toByteArray().length);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOG.error("Cannot compress bytes, error: ", e);
        }
        return null;
    }

    private byte[] deCompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot compress bytes, error: ", e);
        }
        return null;
    }
}
