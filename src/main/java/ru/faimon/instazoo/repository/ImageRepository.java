package ru.faimon.instazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.faimon.instazoo.entity.ImageModel;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long userId);

    Optional<ImageModel> findByPostId(Long postId);
}
