package com.fierceadventurer.postservice.repository;
import com.fierceadventurer.postservice.dto.PostResponseDto;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>  {
    Optional<Post> findByIdAndStatusNot(UUID id, PostStatus status);
    Page<Post> findAllByStatusNot(PostStatus status, Pageable pageable);
}
