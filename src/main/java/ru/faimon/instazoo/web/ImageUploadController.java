package ru.faimon.instazoo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.faimon.instazoo.entity.ImageModel;
import ru.faimon.instazoo.payload.response.MessageResponse;
import ru.faimon.instazoo.service.ImageUploadService;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/images")
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/")
    public ResponseEntity<MessageResponse> uploadImageForUser(@RequestParam("file") MultipartFile file,
                                                              Principal principal) throws IOException {
        imageUploadService.uploadImageForUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<MessageResponse> uploadImageForPost(@RequestParam("file") MultipartFile file,
                                                              Principal principal,
                                                              @PathVariable("postId") String postId) throws IOException {
        imageUploadService.uploadImageForPost(file, principal, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal) {
        ImageModel imageModel = imageUploadService.getImageForUser(principal);
        return ResponseEntity.ok(imageModel);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ImageModel> getImageForPost(@PathVariable("postId") String postId) {
        ImageModel imageModel = imageUploadService.getImageForPost(Long.parseLong(postId));
        return ResponseEntity.ok(imageModel);
    }
}
